package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGroupRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;
}
