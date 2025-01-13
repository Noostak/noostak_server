package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.auth.common.AuthException;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtRefreshTokenProviderTest {

    private JwtRefreshTokenProvider jwtRefreshTokenProvider;
    private JwtTokenProvider jwtTokenProvider;
    private Clock fixedClock;
    private final String jwtSecret = "my-refresh-token-secret-key-my-refresh-token-secret-key";

    private final long REFRESH_TOKEN_EXPIRATION_TIME = 3600000L;

    @BeforeEach
    void setUp() {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setSigningKey(signingKey);

        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        jwtRefreshTokenProvider = new JwtRefreshTokenProvider(jwtTokenProvider, fixedClock);
    }

    @Test
    @DisplayName("리프레시 토큰 발급 - 정상적인 경우")
    void issueToken_shouldReturnValidRefreshToken() {
        // When
        String token = jwtRefreshTokenProvider.issueToken();

        // Then
        Claims claims = jwtRefreshTokenProvider.getClaimsFromToken(token);
        assertThat(claims).isNotNull();
        assertThat(claims.getId()).isNotBlank();

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        assertThat(Math.abs(issuedAt.getTime() - Date.from(fixedClock.instant()).getTime()))
                .isLessThan(1000);

        assertThat(Math.abs(expiration.getTime() - Date.from(fixedClock.instant().plusMillis(REFRESH_TOKEN_EXPIRATION_TIME)).getTime()))
                .isLessThan(1000);
    }

    @Test
    @DisplayName("리프레시 토큰 클레임 추출 - 정상적인 토큰")
    void getClaimsFromToken_shouldReturnClaimsForValidToken() {
        // Given
        String token = jwtRefreshTokenProvider.issueToken();

        // When
        Claims claims = jwtRefreshTokenProvider.getClaimsFromToken(token);

        // Then
        assertThat(claims).isNotNull();
        assertThat(claims.getId()).isNotBlank();
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 - 잘못된 서명")
    void getClaimsFromToken_shouldThrowExceptionForInvalidSignature() {
        // Given
        String validHeaderPayload = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VySWQiLCJpYXQiOjE2MjkxMjg2MDAsImV4cCI6MTYyOTEyODk2MH0";
        String invalidToken = validHeaderPayload + ".invalid-signature";

        // When & Then
        AuthException exception = assertThrows(AuthException.class, () ->
                jwtRefreshTokenProvider.getClaimsFromToken(invalidToken));
        assertThat(exception.getCause()).isInstanceOf(io.jsonwebtoken.SignatureException.class);
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 - 만료된 토큰")
    void getClaimsFromToken_shouldThrowExceptionForExpiredToken() {
        // Given
        Instant pastInstant = fixedClock.instant().minusMillis(REFRESH_TOKEN_EXPIRATION_TIME + 1000L);
        Clock expiredClock = Clock.fixed(pastInstant, ZoneId.systemDefault());

        JwtRefreshTokenProvider expiredProvider = new JwtRefreshTokenProvider(jwtTokenProvider, expiredClock);

        String expiredToken = expiredProvider.issueToken();

        // When & Then
        AuthException exception = assertThrows(AuthException.class, () ->
                jwtRefreshTokenProvider.getClaimsFromToken(expiredToken));
        assertThat(exception.getCause()).isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
