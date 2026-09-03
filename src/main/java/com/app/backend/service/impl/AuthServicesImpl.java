package com.app.backend.service.impl;

import com.app.backend.config.AppProperties;
import com.app.backend.constant.RedisKey;
import com.app.backend.dtos.request.LoginRequest;
import com.app.backend.dtos.request.RegisterRequest;
import com.app.backend.dtos.request.UserRequest;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.UserResponse;
import com.app.backend.entity.ManagerGroup;
import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.AuthContextService;
import com.app.backend.service.CacheService;
import com.app.backend.service.JwtService;
import com.app.backend.service.RedisService;
import com.app.backend.service.intf.AuthServices;
import io.jsonwebtoken.Claims;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServicesImpl implements AuthServices {
    private final UserServiceImpl userService;
    private final AppProperties appProperties;

    private final JwtService jwtService;
    private final EmailServiceImpl emailService;

    private final AuthenticationManager authenticationManager;
    private final CacheService cacheService;

    private static final Duration OTP_CACHE_TTL = Duration.ofMinutes(5);


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
            cacheService.setCache(
                    RedisKey.refreshTokenTypeById(user.getId()),
                    refreshToken,
                    Duration.ofSeconds(appProperties.getJwt().getRefreshTokenExpireSeconds())//RT_CACHE_TTL
            );

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
        }
//        catch (AuthenticationException ex) {
//            throw new CommonException(ErrorCode.AUTH_WRONG);
//        }
        catch (BadCredentialsException ex) {
            throw new CommonException(ErrorCode.AUTH_WRONG);
        } catch (LockedException ex) {
            throw new CommonException(ErrorCode.AUTH_ACCOUNT_LOCKED);
        } catch (DisabledException ex) {
            throw new CommonException(ErrorCode.AUTH_ACCOUNT_DISABLED);
        } catch (AuthenticationException ex) {
            throw new CommonException(ErrorCode.AUTH_WRONG);
        }
    }

    @Override
    public User register(UserRequest request) {
        User u  = userService.createSaveDB(request, Status.LOCKED);
        String otp = String.format("%06d", new Random().nextInt(999999));

        cacheService.setCache(
                RedisKey.otpTypeById(request.getUsername()),
                otp,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );
        String subject = "Mã OTP kích hoạt email";

        String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px;">
            <h2 style="color: #333;">Kích hoạt email</h2>

            <p>Xin chào,</p>

            <p>
                Bạn đang thực hiện kích hoạt email cho tài khoản của mình.
                Vui lòng sử dụng mã OTP bên dưới để hoàn tất:
            </p>

            <div style="
                margin: 24px 0;
                padding: 16px;
                text-align: center;
                background-color: #f5f5f5;
                border-radius: 8px;
                font-size: 32px;
                font-weight: bold;
                letter-spacing: 8px;
                color: #222;
            ">
                %s
            </div>

            <p>
                Mã OTP có hiệu lực trong <strong>5 phút</strong>.
            </p>

            <p>
                Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.
            </p>

            <p style="color: #888; font-size: 12px; margin-top: 32px;">
                Đây là email được gửi tự động, vui lòng không trả lời email này.
            </p>
        </div>
        """.formatted(otp);


        emailService.sendEmail(request.getEmail(), subject, html);

        return u;
    }

    @Override
    public void changePassword(String username, String newPassword) {
        cacheService.evict(RedisKey.refreshTokenTypeById(AuthContextService.getContext().getUserId()));
    }

    @Override
    public void forgotPassword(String username) {

    }

    @Override
    public AuthResponse logout(HttpServletResponse response) {
        cacheService.evict(RedisKey.refreshTokenTypeById(AuthContextService.getContext().getUserId()));
        ResponseCookie cookie = ResponseCookie.from("rf-tk", null)
                .httpOnly(true)
                .secure(appProperties.getCookie().isSecure())
                .path("/api/v1/authen/refresh")
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
    public AuthResponse refresh(String refreshToken, HttpServletResponse response) {

        Claims claims = jwtService.parseRefreshToken(refreshToken);


        String username = claims.get("username", String.class);
        Integer userId = claims.get("userId", Integer.class);
        List<String> roles = claims.get("roles", List.class);

        if(!Objects.equals(refreshToken, cacheService.getCache(RedisKey.refreshTokenTypeById(userId), String.class))) {
            throw new CommonException(ErrorCode.AUTH_RESET_TOKEN_NOTMATCH);
        }

        long ttl = jwtService.getRemainingTime(claims);
        if (ttl <= 0) {
            throw new CommonException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

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

        String newAT = jwtService.generateAccesstoken(user);

        String newRT = jwtService.generateRefreshToken(user);
        cacheService.setCache(
                RedisKey.refreshTokenTypeById(user.getId()),
                newRT,
                Duration.ofSeconds(appProperties.getJwt().getRefreshTokenExpireSeconds())//RT_CACHE_TTL
        );
        ResponseCookie cookie = ResponseCookie.from("rf-tk", newRT)
                .httpOnly(true)
                .secure(appProperties.getCookie().isSecure())
                .path("/api/v1/authen/refresh")
                .maxAge(appProperties.getJwt().getRefreshTokenExpireSeconds())
                .sameSite(appProperties.getCookie().getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return AuthResponse.builder()
                .accessToken(newAT)
                .expiresIn(appProperties.getJwt().getAccessTokenExpireSeconds())
                .build();
    }

    @Override
    public void activeAccount(RegisterRequest request) {

        String otp = cacheService.getCache(RedisKey.otpTypeById(request.getUsername()), String.class);

        if(otp.equals(request.getOtp())){
            evictOTPCache(request.getUsername());
            userService.activeUser(request.getUsername());
        }else {
            throw new CommonException(ErrorCode.AUTH_OTP_NOTMATCH);
        }
    }

    private void evictOTPCache(String username) {

        try {
            cacheService.evict(RedisKey.otpTypeById(username));
        } catch (Exception ignored) {
        }
    }

    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
