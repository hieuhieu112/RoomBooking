package com.app.backend.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModelResponse {
    private Integer id;
    private String name;
    private Integer deviceTypeId;
    private Integer manufacturerDeviceId;
    private String specifications;
}
