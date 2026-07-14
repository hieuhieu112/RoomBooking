package com.app.backend.dtos.internal;


import java.util.Set;

public class AuthContext {

    private final String username;
    private final Long userId;
    private final Set<String> roles;

    public AuthContext(String username, Long userId, Set<String> roles) {
        this.username = username;
        this.roles = roles;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public Long getUserId() {
        return userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean hasRole(String role) {
        return roles.contains("ROLE_" + role);
    }

    public boolean hasAnyRole(String... roles) {
        for (String r : roles) {
            if (hasRole(r)) return true;
        }
        return false;
    }
}
