package com.app.backend.controller;

import com.app.backend.dtos.request.LoginRequest;
import com.app.backend.dtos.response.AuthResponse;
import com.app.backend.dtos.response.DataResponse;
import com.app.backend.dtos.response.StatusRes;
import com.app.backend.service.impl.AuthServicesImpl;
import com.app.backend.utils.ValidRequestUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/v1/authen")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServicesImpl authServices;

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
    public ResponseEntity<DataResponse<String>> logout(HttpServletResponse response){
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
            @CookieValue(name = "rf-tk", required = false) String refreshToken
    ){
        AuthResponse authResponse = authServices.refresh(refreshToken);

        return ResponseEntity.ok(DataResponse.<AuthResponse>builder()
                        .data(authResponse)
                        .statusCode("200")
                        .message(StatusRes.SUCCESS)
                .build()
        );
    }
}
