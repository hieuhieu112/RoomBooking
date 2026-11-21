package com.app.backend.controller;

import com.app.backend.config.AppProperties;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.DataResponse;
import com.app.backend.dtos.response.StatusRes;
import com.app.backend.dtos.response.UserResponse;
import com.app.backend.service.impl.AuthServicesImpl;
import com.app.backend.service.impl.UserServiceImpl;
import com.app.backend.service.intf.AuthServices;
import com.app.backend.service.intf.UserService;
import com.app.backend.utils.ValidRequestUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/authen")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthServices authServices;
    private final AppProperties appProperties;

    @PostMapping("/login")
    public ResponseEntity<DataResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                            BindingResult result,
                                                            HttpServletResponse response

    ){
        ValidRequestUtil.validateRequest(result);
        AuthResponse rsDataAuthen = authServices.login(loginRequest,response);

        DataResponse<AuthResponse> responseAuth = DataResponse.<AuthResponse>builder()
                .data(rsDataAuthen)
                .statusCode("200")
                .message(StatusRes.SUCCESS)
                .build();
        return ResponseEntity.ok(responseAuth) ;
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<DataResponse<String>> logout(
            HttpServletResponse response
    ){
        authServices.logout(response);

        DataResponse<String> resp = DataResponse.<String>builder()
                .statusCode("200")
                .message("Bạn đã đăng xuất")
                .data(null)
                .build();

        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<DataResponse<AuthResponse>> refreshAT(
            @CookieValue(name = "rf-tk") String refreshToken,
            HttpServletResponse response
    ){
        AuthResponse authResponse = authServices.refresh(refreshToken,response);

        return ResponseEntity.ok(DataResponse.<AuthResponse>builder()
                        .data(authResponse)
                        .statusCode("200")
                        .message(StatusRes.SUCCESS)
                .build()
        );
    }


    @PatchMapping("/change_pass")
    public ResponseEntity<DataResponse<Void>> changPasswordMyAccount(
            @RequestBody ChangePasswordRequest request, BindingResult result
    ){
        ValidRequestUtil.validateRequest(result);
        authServices.changePasswordMyAccount(request);
        return ResponseEntity.ok(DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS)
                .message("succcess")
                .build()
        );

    }

    @PostMapping(value = "/register")
    public ResponseEntity<DataResponse<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request, BindingResult result
    ){
        ValidRequestUtil.validateRequest(result);
        DataResponse<UserResponse> response = DataResponse.<UserResponse>builder()
                .data(userService.mapToResponse(authServices.register(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/active")
    public ResponseEntity<DataResponse<Void>> active(
            @Valid @RequestBody RegisterRequest request, BindingResult result
    ){
        ValidRequestUtil.validateRequest(result);
        authServices.activeAccount(request);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .data(null)
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/forget-password")
    public ResponseEntity<DataResponse<Void>> forgetPassword(
            @Valid @RequestBody ForgetPassRequest request, BindingResult result
    ){
        ValidRequestUtil.validateRequest(result);
        authServices.forgotPassword(request);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .data(null)
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/confirm-forget-password")
    public ResponseEntity<DataResponse<Void>> confirmForgetPassword(
            @Valid @RequestBody ConfirmForgetPassRequest request, BindingResult result
    ){
        ValidRequestUtil.validateRequest(result);
        authServices.confirmForgotPassword(request);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .data(null)
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }
}
