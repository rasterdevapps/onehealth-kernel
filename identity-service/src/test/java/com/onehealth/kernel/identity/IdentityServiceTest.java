package com.onehealth.kernel.identity;

import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.kernel.identity.dto.*;
import com.onehealth.kernel.identity.model.User;
import com.onehealth.kernel.identity.model.UserStatus;
import com.onehealth.kernel.identity.repository.RefreshTokenRepository;
import com.onehealth.kernel.identity.repository.UserRepository;
import com.onehealth.kernel.identity.security.JwtTokenProvider;
import com.onehealth.kernel.identity.service.IdentityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private IdentityService identityService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId("TENANT_001");
    }

    @Test
    void register_shouldCreateUser_whenUsernameAndEmailAreUnique() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "password123", "John", "Doe");

        when(userRepository.existsByUsernameAndTenantId("john", "TENANT_001")).thenReturn(false);
        when(userRepository.existsByEmailAndTenantId("john@example.com", "TENANT_001")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");

        User savedUser = new User();
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");
        savedUser.setPasswordHash("hashed_password");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setStatus(UserStatus.ACTIVE);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = identityService.register(request);

        assertThat(response.username()).isEqualTo("john");
        assertThat(response.email()).isEqualTo("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrow_whenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "password123", "John", "Doe");
        when(userRepository.existsByUsernameAndTenantId("john", "TENANT_001")).thenReturn(true);

        assertThatThrownBy(() -> identityService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("john", "password123");

        User user = new User();
        user.setUsername("john");
        user.setPasswordHash("hashed_password");
        user.setStatus(UserStatus.ACTIVE);
        user.setFailedLoginAttempts(0);

        when(userRepository.findByUsernameAndTenantId("john", "TENANT_001"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString(), any()))
                .thenReturn("access_token");
        when(jwtTokenProvider.getAccessTokenExpirySeconds()).thenReturn(900L);
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any())).thenReturn(user);

        TokenResponse response = identityService.login(request, "127.0.0.1", "test-agent");

        assertThat(response.accessToken()).isEqualTo("access_token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(900L);
    }

    @Test
    void login_shouldThrow_whenUserNotFound() {
        LoginRequest request = new LoginRequest("unknown", "password");
        when(userRepository.findByUsernameAndTenantId("unknown", "TENANT_001"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> identityService.login(request, "127.0.0.1", "agent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void login_shouldThrow_whenAccountIsLocked() {
        LoginRequest request = new LoginRequest("john", "password123");

        User user = new User();
        user.setUsername("john");
        user.setStatus(UserStatus.LOCKED);

        when(userRepository.findByUsernameAndTenantId("john", "TENANT_001"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> identityService.login(request, "127.0.0.1", "agent"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("locked");
    }
}
