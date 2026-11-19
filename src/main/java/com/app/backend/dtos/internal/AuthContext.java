package com.app.backend.dtos.internal;


import java.util.Set;

public class AuthContext {

    private final String username;
    private final Integer userId;
    private final Set<String> roles;
    private final String sid;
    private final Integer authVersion;

    public AuthContext(String username, Integer userId, Set<String> roles, String sid, Integer authVersion) {
        this.username = username;
        this.roles = roles;
        this.userId = userId;
        this.sid = sid;
        this.authVersion = authVersion;
    }


    public AuthContext(String username, Integer userId, Set<String> roles) {
        this.username = username;
        this.roles = roles;
        this.userId = userId;
        this.sid = null;
        this.authVersion = null;
    }

    //
    public String getSid() {
        return sid;
    }

    public Integer getAuthVersion() {
        return authVersion;
    }

    public String getUsername() {
        return username;
    }
    public Integer getUserId() {
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


    public boolean isFullAccess() {

        return roles.contains("ROLE_SYSTEM_ADMIN");
    }
}
