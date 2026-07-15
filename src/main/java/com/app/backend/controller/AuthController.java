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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
