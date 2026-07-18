package com.app.backend.service.impl;

import com.app.backend.config.AppProperties;
import com.app.backend.dtos.request.LoginRequest;
import com.app.backend.dtos.request.UserRequest;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.UserResponse;
import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.JwtService;
import com.app.backend.service.intf.AuthServices;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServicesImpl implements AuthServices {
    private final UserServiceImpl userService;
    private final AppProperties appProperties;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = (User) auth.getPrincipal();

            user.validStatus();
            String accessToken = jwtService.generateAccesstoken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            ResponseCookie cookie = ResponseCookie.from("rf-tk", refreshToken)
                    .httpOnly(true)
                    .secure(appProperties.getCookie().isSecure())
                    .path("/api/v1/authen/refresh")
                    .maxAge(appProperties.getJwt().getRefreshTokenExpireSeconds())
                    .sameSite(appProperties.getCookie().getSameSite())
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            System.out.println(cookie.toString());
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .user(UserResponse.convertFromEntity(user))
                    .expiresIn(appProperties.getJwt().getAccessTokenExpireSeconds())
                    .build();
        } catch (AuthenticationException ex) {
            throw new CommonException(ErrorCode.AUTH_WRONG);
        }
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        return userService.mapToResponse(userService.create(userRequest));
    }

    @Override
    public void changePassword(String username, String newPassword) {

    }

    @Override
    public void forgotPassword(String username) {

    }

    @Override
    public AuthResponse logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("rf-tk", null)
                .httpOnly(true)
                .secure(appProperties.getCookie().isSecure())
                .path("/")
                .maxAge(appProperties.getJwt().getRefreshTokenExpireSeconds())
                .sameSite(appProperties.getCookie().getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return AuthResponse.builder()
                .accessToken(null)
                .user(null)
                .expiresIn(appProperties.getJwt().getAccessTokenExpireSeconds())
                .build();
    }

    @Override
    public AuthResponse refresh(String refreshToken) {

        Claims claims = jwtService.parseRefreshToken(refreshToken);


        String username = claims.get("username", String.class);
        Integer userId = claims.get("userId", Integer.class);
        List<String> roles = claims.get("roles", List.class);

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(
                roles.stream()
                        .map(roleName -> {
                            Role role = new Role();
                            role.setName(roleName);
                            return role;
                        })
                        .collect(Collectors.toSet())
        );
        long ttl = jwtService.getRemainingTime(claims);
        if (ttl <= 0) {
            throw new CommonException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

        String newAT = jwtService.generateAccesstoken(user);

        return AuthResponse.builder()
                .accessToken(newAT)
                .expiresIn(appProperties.getJwt().getAccessTokenExpireSeconds())
                .build();
    }

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
