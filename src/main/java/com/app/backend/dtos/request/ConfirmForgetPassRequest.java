package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmForgetPassRequest {
    @NotBlank(message = "username không được để trống")
    private String username;

    @NotBlank(message = "Mat khau không được để trống")
    @Size(min = 6, message = "Mat khau phai dai hon 6 ky tu")
    private String password;

    @NotBlank(message = "OTP khong duoc de trong")
    @Size(min = 6, message = "OTP phai dai hon 6 ky tu")
    private String otp;
}
