package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.DeviceIndecentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentRequest {
    private String description;
    private LocalDateTime timeIncident;
    private DeviceIndecentStatus status;
    private Integer managerId;
    private Integer bookingId;
    private Integer incidentBy;
}
