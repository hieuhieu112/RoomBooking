package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.DeviceIndecentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentRequest {

    private String description;

    @NotBlank(message = "Thoi gian không được để trống")
    private LocalDateTime timeIncident;
    private DeviceIndecentStatus status;

    @NotBlank(message = "Thong tin quan ly không được để trống")
    private Integer managerId;

    @NotBlank(message = "Thong tin boooking không được để trống")
    private Integer bookingId;

    @NotBlank(message = "Thong tin nguoi vi pham không được để trống")
    private Integer incidentBy;
}
