package com.onehealth.kernel.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtTenantConverterTest {

    private final JwtTenantConverter converter = new JwtTenantConverter();

    private Jwt buildJwt(Map<String, Object> claims) {
        return Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject(claims.getOrDefault("sub", "user1").toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claims(c -> c.putAll(claims))
                .build();
    }

    @Test
    void shouldConvertJwtToAuthenticationToken() {
        Jwt jwt = buildJwt(Map.of("sub", "user1", "tenant_id", "TENANT_001"));
        JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);
        assertNotNull(token);
        assertEquals("user1", token.getName());
    }

    @Test
    void shouldExtractRolesAsGrantedAuthorities() {
        Jwt jwt = buildJwt(Map.of("sub", "user1", "tenant_id", "T1",
                "roles", List.of("ADMIN", "USER")));
        JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldHandleJwtWithoutRoles() {
        Jwt jwt = buildJwt(Map.of("sub", "user1", "tenant_id", "T1"));
        JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);
        assertNotNull(token);
        assertTrue(token.getAuthorities().isEmpty());
    }
}
