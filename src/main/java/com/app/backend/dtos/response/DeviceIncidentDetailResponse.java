package com.app.backend.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentDetailResponse {
    private Long id;
    private Integer deviceIncidentId;
    private Integer deviceId;
}
