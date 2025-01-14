package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtAccessTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtClaimsBuilder jwtClaimsBuilder;
    private final String jwtSecret = "my-test-secret-key-my-test-secret-key";

    @BeforeEach
    void setUp() {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setSigningKey(signingKey);
        jwtClaimsBuilder = new DefaultJwtClaimsBuilder();
    }

    private Clock createClock(Instant instant) {
        return Clock.fixed(instant, ZoneId.of("UTC"));
    }

    private JwtAccessTokenProvider createJwtAccessTokenProvider(Clock clock, long expirationTime) {
        return new JwtAccessTokenProvider(jwtTokenProvider, clock, jwtClaimsBuilder);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(String username) {
        return new UsernamePasswordAuthenticationToken(username, null);
    }

    @Test
    @DisplayName("액세스 토큰 발급 - 정상적인 입력값")
    void issueToken_shouldReturnValidToken() {
        // Given
        Instant fixedInstant = Instant.parse("2026-01-01T12:00:00Z");
        Clock testClock = createClock(fixedInstant);
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(testClock, 3600000L);

        UsernamePasswordAuthenticationToken authentication = createAuthentication("testUser");
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        // When
        String token = provider.issueToken(authentication, roles);

        // Then
        Claims claims = provider.getClaimsFromToken(token);
        assertThat(claims.getSubject()).isEqualTo("testUser");
        assertThat(claims.get("roles", List.class)).containsExactly("ROLE_USER", "ROLE_ADMIN");
        assertThat(Math.abs(claims.getIssuedAt().getTime() - fixedInstant.toEpochMilli()))
                .isLessThan(1000);
        assertThat(Math.abs(claims.getExpiration().getTime() - fixedInstant.plusMillis(3600000L).toEpochMilli()))
                .isLessThan(1000);
    }

    @Test
    @DisplayName("액세스 토큰 발급 - roles가 없는 경우")
    void issueToken_shouldHandleEmptyRoles() {
        // Given
        Instant fixedInstant = Instant.parse("2026-01-01T12:00:00Z");
        Clock testClock = createClock(fixedInstant);
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(testClock, 3600000L);

        UsernamePasswordAuthenticationToken authentication = createAuthentication("testUser");
        List<String> roles = List.of();

        // When
        String token = provider.issueToken(authentication, roles);

        // Then
        Claims claims = provider.getClaimsFromToken(token);
        assertThat(claims.getSubject()).isEqualTo("testUser");
        assertThat(claims.get("roles", List.class)).isEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 - 잘못된 서명")
    void getClaimsFromToken_shouldThrowExceptionForInvalidSignature() {
        // Given
        Instant fixedInstant = Instant.parse("2025-01-01T12:00:00Z");
        Clock testClock = createClock(fixedInstant);
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(testClock, 3600000L);

        String invalidToken = "invalid.token.string";

        // When & Then
        assertThrows(Exception.class, () -> provider.getClaimsFromToken(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰 검증")
    void getClaimsFromToken_shouldThrowExceptionForExpiredToken() {
        // Given
        Instant fixedInstant = Instant.parse("2025-01-01T12:00:00Z");
        Clock expiredClock = createClock(fixedInstant.minusSeconds(3600));
        JwtAccessTokenProvider expiredProvider = createJwtAccessTokenProvider(expiredClock, 1000L);

        UsernamePasswordAuthenticationToken authentication = createAuthentication("testUser");
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        String expiredToken = expiredProvider.issueToken(authentication, roles);

        // When & Then
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () ->
                expiredProvider.getClaimsFromToken(expiredToken));
    }
}