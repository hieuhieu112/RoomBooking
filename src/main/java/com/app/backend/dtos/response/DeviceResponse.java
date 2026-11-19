package com.app.backend.dtos.response;

import com.app.backend.entity.enumm.DeviceTrackingType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private Integer id;
    private String name;
    private String status;
    private Integer deviceCategoryId;
    private Integer deviceModelId;
    private Integer quantity;
    private String serial;
    private DeviceTrackingType trackingType;

//    private Integer roomId;
}
