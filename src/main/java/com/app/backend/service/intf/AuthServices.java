package com.app.backend.service.intf;

import com.app.backend.dtos.request.LoginRequest;
import com.app.backend.dtos.request.RegisterRequest;
import com.app.backend.dtos.request.UserRequest;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.UserResponse;
import com.app.backend.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthServices {
    AuthResponse login(LoginRequest loginRequest, HttpServletResponse response);
    User register(UserRequest userRequest);
    AuthResponse logout(HttpServletResponse response);
    AuthResponse refresh(String refreshToken, HttpServletResponse response);
    void activeAccount(RegisterRequest request);

    void changePassword(String username, String newPassword);
    void forgotPassword(String username);
}
