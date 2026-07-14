package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModelRequest {
    private String name;
    private Integer deviceTypeId;
    private Integer manufacturerDeviceId;
    private String specifications;
}
