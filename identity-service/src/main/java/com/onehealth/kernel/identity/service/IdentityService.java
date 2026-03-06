package com.onehealth.kernel.identity.service;

import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.kernel.identity.dto.*;
import com.onehealth.kernel.identity.model.RefreshToken;
import com.onehealth.kernel.identity.model.User;
import com.onehealth.kernel.identity.model.UserStatus;
import com.onehealth.kernel.identity.repository.RefreshTokenRepository;
import com.onehealth.kernel.identity.repository.UserRepository;
import com.onehealth.kernel.identity.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IdentityService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 30;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public IdentityService(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        String tenantId = TenantContext.getTenantId();
        if (userRepository.existsByUsernameAndTenantId(request.username(), tenantId)) {
            throw new IllegalArgumentException("Username already exists in this tenant");
        }
        if (userRepository.existsByEmailAndTenantId(request.email(), tenantId)) {
            throw new IllegalArgumentException("Email already exists in this tenant");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        return toResponse(user);
    }

    public TokenResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String tenantId = TenantContext.getTenantId();
        User user = userRepository.findByUsernameAndTenantId(request.username(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new IllegalStateException("Account is locked");
        }
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Account is inactive");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setStatus(UserStatus.LOCKED);
            }
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(), tenantId, List.of("USER"));

        String rawRefreshToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setTokenHash(hashToken(rawRefreshToken));
        refreshToken.setExpiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_DAYS * 86400));
        refreshToken.setIpAddress(ipAddress);
        refreshToken.setUserAgent(userAgent);
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.of(accessToken, rawRefreshToken, jwtTokenProvider.getAccessTokenExpirySeconds());
    }

    public TokenResponse refresh(RefreshRequest request) {
        String tokenHash = hashToken(request.refreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException("Refresh token has been revoked");
        }
        if (refreshToken.isExpired()) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("User account is not active");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(), refreshToken.getTenantId(), List.of("USER"));

        return TokenResponse.of(accessToken, request.refreshToken(), jwtTokenProvider.getAccessTokenExpirySeconds());
    }

    public void logout(String username) {
        String tenantId = TenantContext.getTenantId();
        userRepository.findByUsernameAndTenantId(username, tenantId).ifPresent(user ->
                refreshTokenRepository.revokeAllByUserIdAndTenantId(user.getId(), tenantId));
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        String tenantId = TenantContext.getTenantId();
        User user = userRepository.findByUsernameAndTenantId(username, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByUsernameAndTenantId(username, tenantId)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus(),
                user.getTenantId(),
                user.getCreatedAt()
        );
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
