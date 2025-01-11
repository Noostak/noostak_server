package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    private final long refreshTokenExpirationTime = 3600000L; // 1시간

    @BeforeEach
    void setUp() {
        // SecretKey 생성
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        // JwtTokenProvider 초기화
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setSigningKey(signingKey);

        // 고정된 Clock 설정
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // JwtRefreshTokenProvider 초기화
        jwtRefreshTokenProvider = new JwtRefreshTokenProvider(jwtTokenProvider, fixedClock);
        jwtRefreshTokenProvider.setRefreshTokenExpirationTime(refreshTokenExpirationTime); // 명시적 값 설정
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

        // 발급 시점과 만료 시점 검증
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        assertThat(Math.abs(issuedAt.getTime() - Date.from(fixedClock.instant()).getTime()))
                .isLessThan(1000); // 1초 이내의 차이를 허용

        assertThat(Math.abs(expiration.getTime() - Date.from(fixedClock.instant().plusMillis(refreshTokenExpirationTime)).getTime()))
                .isLessThan(1000); // 1초 이내의 차이를 허용
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
        String invalidToken = "invalid.token.string";

        // When & Then
        assertThrows(Exception.class, () -> jwtRefreshTokenProvider.getClaimsFromToken(invalidToken));
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 - 만료된 토큰")
    void getClaimsFromToken_shouldThrowExceptionForExpiredToken() {
        // Given
        Instant pastInstant = fixedClock.instant().minusMillis(refreshTokenExpirationTime + 1000L); // 만료 시간 초과
        Clock expiredClock = Clock.fixed(pastInstant, ZoneId.systemDefault());

        JwtRefreshTokenProvider expiredProvider = new JwtRefreshTokenProvider(jwtTokenProvider, expiredClock);
        expiredProvider.setRefreshTokenExpirationTime(refreshTokenExpirationTime); // 값 설정 추가

        // Expired token 생성
        String expiredToken = expiredProvider.issueToken();

        // When & Then
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () ->
                jwtRefreshTokenProvider.getClaimsFromToken(expiredToken));
    }
}