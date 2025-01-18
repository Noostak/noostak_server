package org.noostak.server.auth.application.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthentication extends AbstractAuthenticationToken {

    private final String userId;

    public CustomAuthentication(String userId, Object credentials, Object details) {
        super(null);
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}