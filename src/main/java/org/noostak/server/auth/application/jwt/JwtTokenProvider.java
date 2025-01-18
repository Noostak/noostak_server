package org.noostak.server.auth.application.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.noostak.server.auth.common.AuthErrorCode;
import org.noostak.server.auth.common.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Getter
    @Setter
    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        String encodedKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        this.signingKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_JWT_TOKEN, e);
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN, e);
        }
    }

    public String getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
