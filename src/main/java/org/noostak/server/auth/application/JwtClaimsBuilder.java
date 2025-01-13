package org.noostak.server.auth.application;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface JwtClaimsBuilder {
    Claims buildClaims(Authentication authentication, List<String> roles, Date now, Date expiration);
}
