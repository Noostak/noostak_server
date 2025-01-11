package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtRefreshTokenProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final Clock clock;

    @Value("${jwt.expiration.refresh-token}")
    @Setter
    private Long refreshTokenExpirationTime;

    public String issueToken() {
        final Instant nowInstant = clock.instant();
        final Date now = Date.from(nowInstant); // Instant → Date 변환
        final Date expiration = Date.from(nowInstant.plusMillis(refreshTokenExpirationTime));

        final Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiration);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(jwtTokenProvider.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtTokenProvider.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
