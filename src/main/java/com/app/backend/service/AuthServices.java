package com.app.backend.service;

import com.app.backend.dtos.request.LoginRequest;
import com.app.backend.dtos.request.UserRequest;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthServices {
    AuthResponse login(LoginRequest loginRequest, HttpServletResponse response);
    UserResponse createUser(UserRequest userRequest);
    void changePassword(String username, String newPassword);
    void forgotPassword(String username);
    AuthResponse logout(HttpServletResponse response);
    AuthResponse refresh(String refreshToken);
}
