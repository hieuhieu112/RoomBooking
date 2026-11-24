package com.app.backend.service.impl;

import com.app.backend.config.AppProperties;
import com.app.backend.constant.RedisKey;
import com.app.backend.dtos.internal.AuthContext;
import com.app.backend.dtos.request.*;
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
import com.app.backend.service.intf.AuthSessionStore;
import com.app.backend.service.intf.UserService;
import io.jsonwebtoken.Claims;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.apache.poi.hpsf.GUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServicesImpl implements AuthServices {
    private final UserService userService;
    private final AppProperties appProperties;

    private final JwtService jwtService;
    private final EmailServiceImpl emailService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthSessionStore authSessionStore;
    private final CacheService cacheService;

    private static final Duration OTP_CACHE_TTL = Duration.ofMinutes(5);

    private static final int numberTryOTP = 5;


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
            String sid = UUID.randomUUID().toString();
            String accessToken = jwtService.generateAccesstoken(user, sid);
            String refreshToken = jwtService.generateRefreshToken(user, sid);
            authSessionStore.setCacheRefreshToken(
                    user.getId(), user.getAuthVersion(),
                    sid,
                    refreshToken
            );

            ResponseCookie cookie = ResponseCookie.from("rf-tk", refreshToken)
                    .httpOnly(true)
                    .secure(appProperties.getCookie().isSecure())
                    .path("/rba/api/v1/authen/refresh")
                    .maxAge(appProperties.getJwt().getRefreshTokenExpireSeconds())
                    .sameSite(appProperties.getCookie().getSameSite())
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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
    public User register(UserRegisterRequest request) {
        List<String> s = new ArrayList<String>();
        s.add("STUDENT");
        UserRequest userRequest = new UserRequest(request.getName(), request.getEmail(),
                0, request.getPass(), Status.LOCKED, request.getUsername(),
                null, s);
        User u  = userService.createSaveDB(userRequest, Status.LOCKED);
        String otp = String.format("%06d", new SecureRandom().nextInt(999999));

        cacheService.setCache(
                RedisKey.otpTypeById(request.getUsername()),
                otp,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );

        cacheService.setCache(
                RedisKey.tryTimeByID(request.getUsername()),
                0,
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
    public void changePasswordMyAccount(ChangePasswordRequest request){
        AuthContext context =  AuthContextService.getContext();

        User userEntiy = userService.getById(context.getUserId());

        if(passwordEncoder.matches(request.getOldPass(), userEntiy.getPassword())){
            userService.changePass(userEntiy,request.getNewPass());
        }
        else {
            throw new CommonException(ErrorCode.AUTH_WRONG);
        }
    }

    @Override
    public void forgotPassword( ForgetPassRequest request) {
        User userEntity = userService.getByUsername(request.getUsername());

        userEntity.validStatus();

        if(!userEntity.getEmail().equals(request.getEmail()))
        {
            throw new CommonException(ErrorCode.AUTH_EMAIL_NOTMATCH);
        }
        String otp = String.format("%06d", new SecureRandom().nextInt(999999));

        cacheService.setCache(
                RedisKey.otpForgetPassById(request.getUsername()),
                otp,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );

        cacheService.setCache(
                RedisKey.tryTimeByID(request.getUsername()),
                0,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );
        String subject = "Mã OTP lay lai mat khau";

        String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px;">
            <h2 style="color: #333;">Kích hoạt email</h2>

            <p>Xin chào,</p>

            <p>
                Bạn đang thực hiện lay lai mat khau cho tài khoản của mình.
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


    }

    @Override
    public void confirmForgotPassword(ConfirmForgetPassRequest request) {
        Integer tryNumber = cacheService.getCache(RedisKey.tryTimeByID(request.getUsername()), Integer.class);

        if(tryNumber > numberTryOTP){
            evictOTPForgetPassCache(request.getUsername());
            evictOTPTryTime(request.getUsername());
            throw new CommonException(ErrorCode.AUTH_OTP_EXCEED);
        }

        cacheService.setCache(
                RedisKey.tryTimeByID(request.getUsername()),
                tryNumber+1,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );

        String otp = cacheService.getCache(RedisKey.otpForgetPassById(request.getUsername()), String.class);

        if(otp != null && otp.equals(request.getOtp())){
            evictOTPForgetPassCache(request.getUsername());
            User userEntity = userService.getByUsername(request.getUsername());
            userService.changePass(userEntity, request.getPassword());
        }else {
            throw new CommonException(ErrorCode.AUTH_OTP_NOTMATCH);
        }
    }

    @Override
    public AuthResponse logout(HttpServletResponse response) {
        User userEntity = userService.getById(AuthContextService.getContext().getUserId());


        authSessionStore.evictRefreshToken(userEntity.getId(),
                AuthContextService.getContext().getAuthVersion(),
                AuthContextService.getContext().getSid()
        );
        ResponseCookie cookie = ResponseCookie.from("rf-tk", null)
                .httpOnly(true)
                .secure(appProperties.getCookie().isSecure())
                .path("/rba/api/v1/authen/refresh")
                .maxAge(0)
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

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CommonException(ErrorCode.AUTH_TOKEN_INVALID);
        }
        jwtService.validRefreshTokenExpired(refreshToken);

        Claims claims = jwtService.parseRefreshToken(refreshToken);
        Integer userId = jwtService.extractUserIdByClaims(claims);
        String sid = jwtService.extractSidByClaims(claims);
        Integer authVersion = jwtService.extractAuthVersionByClaims(claims);

        User userEntiy = userService.getById(userId);

        userEntiy.validStatus();


        if(
                !Objects.equals(
                        DigestUtils.sha256Hex(refreshToken),
                        authSessionStore.getCacheRefreshToken(userId, authVersion, sid)
                )
                || !Objects.equals(userEntiy.getAuthVersion(), authVersion)
        ) {
            throw new CommonException(ErrorCode.AUTH_RESET_TOKEN_NOTMATCH);
        }

//        List<String> roles = userEntiy.getRoles().stream().map(Role::getName).toList();
//        User user = new User();
//        user.setId(userId);
//
//        user.setUsername(user.getUsername());
//        user.setRoles(
//                roles.stream()
//                        .map(roleName -> {
//                            Role role = new Role();
//                            role.setName(roleName);
//                            return role;
//                        })
//                        .collect(Collectors.toSet())
//        );

        String newAT = jwtService.generateAccesstoken(userEntiy,sid);

        String newRT = jwtService.generateRefreshToken(userEntiy, sid);
        authSessionStore.setCacheRefreshToken(
                userEntiy.getId(), authVersion, sid, newRT
        );
        ResponseCookie cookie = ResponseCookie.from("rf-tk", newRT)
                .httpOnly(true)
                .secure(appProperties.getCookie().isSecure())
                .path("/rba/api/v1/authen/refresh")
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
        Integer tryNumber = cacheService.getCache(RedisKey.tryTimeByID(request.getUsername()), Integer.class);

        if(tryNumber > numberTryOTP){
            evictOTPCache(request.getUsername());
            evictOTPTryTime(request.getUsername());
            throw new CommonException(ErrorCode.AUTH_OTP_EXCEED);
        }

        cacheService.setCache(
                RedisKey.tryTimeByID(request.getUsername()),
                tryNumber+1,
                OTP_CACHE_TTL//RT_CACHE_TTL
        );

        String otp = cacheService.getCache(RedisKey.otpTypeById(request.getUsername()), String.class);

        if(otp != null && otp.equals(request.getOtp())){
            evictOTPCache(request.getUsername());
            userService.activeUser(request.getUsername());
        }else {
            throw new CommonException(ErrorCode.AUTH_OTP_NOTMATCH);
        }
    }

    private void evictOTPCache(String username) {

        cacheService.evict(RedisKey.otpTypeById(username));
    }

    private void evictOTPForgetPassCache(String username) {

        cacheService.evict(RedisKey.otpForgetPassById(username));
    }

    private void evictOTPTryTime(String username) {

        cacheService.evict(RedisKey.tryTimeByID(username));
    }


    private String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
