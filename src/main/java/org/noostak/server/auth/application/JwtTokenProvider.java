package org.noostak.server.auth.application;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

}
