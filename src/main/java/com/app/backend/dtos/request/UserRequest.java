package com.app.backend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;
    private Integer incidentCount;

    @NotBlank(message = "Mat khau không được để trống")
    @Size(min = 6, message = "Mat khau phai dai hon 6 ky tu")
    private String pass;
    private String status;

    @NotBlank(message = "Username không được để trống")
    private String username;
    private Integer managerGroupId;
}
