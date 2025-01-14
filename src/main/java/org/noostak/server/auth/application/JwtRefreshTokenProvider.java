package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.noostak.server.auth.common.AuthErrorCode;
import org.noostak.server.auth.common.AuthException;
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

    private final Long REFRESH_TOKEN_EXPIRATION_TIME = 3600000L;

    private static final String JWT_TYPE = "JWT";

    public String issueToken() {
        final Instant currentInstant = clock.instant();
        final Date currentDate = Date.from(currentInstant);
        final Date expiration = Date.from(currentInstant.plusMillis(REFRESH_TOKEN_EXPIRATION_TIME));

        final Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(currentDate)
                .setExpiration(expiration);

        return Jwts.builder()
                .setHeaderParam("typ", JWT_TYPE)
                .setClaims(claims)
                .signWith(jwtTokenProvider.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims createClaims(Date issuedAt, Date expiration) {
        return Jwts.claims()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration);
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProvider.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_JWT_TOKEN, e);
        } catch (io.jsonwebtoken.SignatureException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN, e);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_JWT_TOKEN, e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.EMPTY_JWT, e);
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN, e);
        }
    }
}
