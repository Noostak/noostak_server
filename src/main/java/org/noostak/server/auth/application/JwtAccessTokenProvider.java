package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class JwtAccessTokenProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final Clock clock;
    private Long accessTokenExpirationTime;

    public JwtAccessTokenProvider(JwtTokenProvider jwtTokenProvider, Clock clock, Long accessTokenExpirationTime) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.clock = clock;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    public String issueToken(Authentication authentication, List<String> roles) {
        final Instant nowInstant = clock.instant();
        final Date now = Date.from(nowInstant);
        final Date expiration = Date.from(nowInstant.plusMillis(accessTokenExpirationTime));

        Claims claims = JwtClaimsBuilder.buildClaims(authentication, roles, now, expiration);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtTokenProvider.getSigningKey())
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtTokenProvider.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    static class JwtClaimsBuilder {
        public static Claims buildClaims(Authentication authentication, List<String> roles, Date now, Date expiration) {
            Claims claims = Jwts.claims();
            claims.setSubject(authentication.getName());
            claims.setIssuedAt(now);
            claims.setExpiration(expiration);
            claims.put("roles", roles);
            return claims;
        }
    }
}