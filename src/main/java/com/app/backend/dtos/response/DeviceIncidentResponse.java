package com.app.backend.dtos.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentResponse {
    private Integer id;
    private String description;
    private LocalDateTime timeIncident;
    private String status;
    private Integer userId;
    private Integer managerId;
    private Integer bookingId;
    private Integer incidentBy;
}
