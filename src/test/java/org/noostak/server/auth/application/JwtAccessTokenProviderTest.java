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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtAccessTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private Clock fixedClock;
    private final String jwtSecret = "my-test-secret-key-my-test-secret-key";

    @BeforeEach
    void setUp() {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setSigningKey(signingKey);

        fixedClock = Clock.offset(Clock.systemDefaultZone(), java.time.Duration.ofMinutes(30));
    }

    private JwtAccessTokenProvider createJwtAccessTokenProvider(long expirationTime) {
        return new JwtAccessTokenProvider(jwtTokenProvider, fixedClock, expirationTime);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(String username) {
        return new UsernamePasswordAuthenticationToken(username, null);
    }

    private List<String> createRoles(String... roles) {
        return Arrays.asList(roles);
    }

    @Test
    @DisplayName("액세스 토큰 발급 - 정상적인 입력값")
    void issueToken_shouldReturnValidToken() {
        // Given
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(3600000L);
        UsernamePasswordAuthenticationToken authentication = createAuthentication("testUser");
        List<String> roles = createRoles("ROLE_USER", "ROLE_ADMIN");

        // When
        String token = provider.issueToken(authentication, roles);

        // Then
        Claims claims = provider.getClaimsFromToken(token);
        assertThat(claims.getSubject()).isEqualTo("testUser");
        assertThat(claims.get("roles", List.class)).containsExactly("ROLE_USER", "ROLE_ADMIN");

        assertThat(Math.abs(claims.getIssuedAt().getTime() - Date.from(fixedClock.instant()).getTime()))
                .isLessThan(1000);
        assertThat(Math.abs(claims.getExpiration().getTime() - Date.from(fixedClock.instant().plusMillis(3600000L)).getTime()))
                .isLessThan(1000);
    }

    @Test
    @DisplayName("액세스 토큰 발급 - roles가 없는 경우")
    void issueToken_shouldHandleEmptyRoles() {
        // Given
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(3600000L);
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
        JwtAccessTokenProvider provider = createJwtAccessTokenProvider(3600000L);
        String invalidToken = "invalid.token.string";

        // When & Then
        assertThrows(Exception.class, () -> provider.getClaimsFromToken(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰 검증")
    void getClaimsFromToken_shouldThrowExceptionForExpiredToken() {
        // Given
        Instant pastInstant = fixedClock.instant().minusMillis(3600000L); // 1시간 전
        Clock expiredClock = Clock.fixed(pastInstant, ZoneId.of("UTC"));

        JwtAccessTokenProvider expiredProvider = new JwtAccessTokenProvider(jwtTokenProvider, expiredClock, 1000L);
        UsernamePasswordAuthenticationToken authentication = createAuthentication("testUser");
        List<String> roles = createRoles("ROLE_USER", "ROLE_ADMIN");
        String expiredToken = expiredProvider.issueToken(authentication, roles);

        // When & Then
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () ->
                expiredProvider.getClaimsFromToken(expiredToken));
    }
}