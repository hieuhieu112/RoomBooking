package com.app.backend.constant;


public class SecurityConstant {
    private SecurityConstant() {}

    public static final String[] PUBLIC_ENDPOINTS = {
            "/roomimages/**",
            "/authen/login",
//            "/authen/logout",
            "/actuator/health",
            "/authen/register",
            "/authen/active",
            "/authen/refresh"
    };
}
