package com.app.backend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;

    private String status;
    private Integer managerGroupId;
}
