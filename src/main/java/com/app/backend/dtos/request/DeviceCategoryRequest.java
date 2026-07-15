package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCategoryRequest {

    @NotBlank(message = "Ten the loai thiet bi không được để trống")
    private String name;
}
