package com.app.backend.dtos.internal;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthDetails  extends WebAuthenticationDetails {
    private final String sid;
    private final Integer authVersion;

    public AuthDetails(HttpServletRequest request, String sid, Integer authVersion) {
        super(request);
        this.sid = sid;
        this.authVersion = authVersion;
    }

    public AuthDetails(String remoteAddress, String sessionId, String sid, Integer authVersion) {
        super(remoteAddress, sessionId);
        this.sid = sid;
        this.authVersion = authVersion;
    }

    public String getSid() {
        return sid;
    }

    public Integer getAuthVersion() {
        return authVersion;
    }
}
