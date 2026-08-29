package com.app.backend.dtos.request;

import com.app.backend.entity.enumm.DeviceStatus;
import com.app.backend.entity.enumm.DeviceTrackingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    private Integer id;

    @NotBlank(message = "Ten không được để trống")
    private String name;
    private DeviceStatus status;

    @NotNull(message = "The loai không được để trống")
    private Integer deviceCategoryId;

    @NotNull(message = "Model thiet bi không được để trống")
    private Integer deviceModelId;

    private Integer quantity;
    private String serial;
    private DeviceTrackingType trackingType;

//    private Integer roomId;
}
