package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentDetailRequest {
    private Integer deviceIncidentId;
    private Integer deviceId;
}
