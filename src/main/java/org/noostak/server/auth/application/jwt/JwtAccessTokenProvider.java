package org.noostak.server.auth.application.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.noostak.server.auth.common.AuthErrorCode;
import org.noostak.server.auth.common.AuthException;
import org.springframework.security.core.Authentication;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class JwtAccessTokenProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final Clock clock;
    private final JwtClaimsBuilder jwtClaimsBuilder;
    private final Long ACCESS_TOKEN_EXPIRATION_TIME = 3600000L;

    private static final String JWT_TYPE = "JWT";

    public String issueToken(Authentication authentication, List<String> roles) {
        final Instant nowInstant = clock.instant();
        final Date now = Date.from(nowInstant);
        final Date expiration = Date.from(nowInstant.plusMillis(ACCESS_TOKEN_EXPIRATION_TIME));

        Claims claims = jwtClaimsBuilder.buildClaims(authentication, roles, now, expiration);

        return Jwts.builder()
                .setHeaderParam("typ", JWT_TYPE)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtTokenProvider.getSigningKey())
                .compact();
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