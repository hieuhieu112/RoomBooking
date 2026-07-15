package com.app.backend.dtos.request;

import com.app.backend.entity.enumm.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;
    private DeviceStatus status;

    @NotBlank(message = "The loai không được để trống")
    private Integer deviceCategoryId;

    @NotBlank(message = "Model thiet bi không được để trống")
    private Integer deviceModelId;

    private Integer roomId;
}
