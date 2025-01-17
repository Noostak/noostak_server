package org.noostak.server.auth.application.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DefaultJwtClaimsBuilder implements JwtClaimsBuilder {

    private static final String ROLES_CLAIM = "roles";

    @Override
    public Claims buildClaims(Authentication authentication, List<String> roles, Date now, Date expiration) {
        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());
        claims.setIssuedAt(now);
        claims.setExpiration(expiration);
        claims.put(ROLES_CLAIM, roles);
        return claims;
    }
}
