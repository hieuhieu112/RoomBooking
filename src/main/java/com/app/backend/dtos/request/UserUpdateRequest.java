package com.app.backend.dtos.request;

import com.app.backend.entity.enumm.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private Integer id;

    @NotBlank(message = "Ten không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email
    private String email;

    private Status status;
    private Integer managerGroupId;

    // ===========field k doi
    private Integer incidentCount=0;
    private String pass;
    private String username;
    private List<String> roles;
}
