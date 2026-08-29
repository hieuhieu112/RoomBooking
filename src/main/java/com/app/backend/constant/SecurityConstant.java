package com.app.backend.constant;


public class SecurityConstant {
    private SecurityConstant() {}

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/roomimages/**",
            "/api/v1/authen/login",
            "/api/v1/authen/register",
            "/api/v1/authen/active",
            "/api/v1/authen/refresh"
    };
}
