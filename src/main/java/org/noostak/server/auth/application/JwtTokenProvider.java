package org.noostak.server.auth.application;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        String encodedKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        this.signingKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
