package com.onehealth.gateway.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Development-only token endpoint. Generates RS256-signed JWTs for testing.
 * In production, this would be replaced by a dedicated OAuth2 Authorization Server.
 */
@RestController
@RequestMapping("/auth")
public class TokenController {

    private static final List<String> DEFAULT_ROLES = List.of("USER");

    private static final long TOKEN_VALIDITY_MS = 3_600_000L; // 1 hour
    public ResponseEntity<Map<String, String>> generateToken(@RequestBody TokenRequest request) {
        try {
            RSAPrivateKey privateKey = loadRsaPrivateKey();
            JWSSigner signer = new RSASSASigner(privateKey);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(request.username())
                    .issuer("onehealth-kernel")
                    .claim("tenant_id", request.tenantId())
                    .claim("roles", request.roles() != null ? request.roles() : DEFAULT_ROLES)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MS))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.RS256),
                    claims);
            signedJWT.sign(signer);

            return ResponseEntity.ok(Map.of("token", signedJWT.serialize()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Token generation failed: " + e.getMessage()));
        }
    }

    private RSAPrivateKey loadRsaPrivateKey() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/private.pem");
        try (InputStream is = resource.getInputStream()) {
            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String encoded = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
        }
    }

    public record TokenRequest(String username, String tenantId, List<String> roles) {}
}
