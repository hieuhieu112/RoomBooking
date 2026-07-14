package com.app.backend.dtos.request;

import com.app.backend.entity.enumm.DeviceStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    private String name;
    private DeviceStatus status;
    private Integer deviceCategoryId;
    private Integer deviceModelId;
    private Integer roomId;
}
