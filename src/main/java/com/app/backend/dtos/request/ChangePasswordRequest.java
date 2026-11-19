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
public class ChangePasswordRequest {
    private String oldPass;
    @NotBlank(message = "Mat khau không được để trống")
    @Size(min = 6, message = "Mat khau phai dai hon 6 ky tu")
    private String newPass;
}
