package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.DeviceIndecentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentRequest {
    private Integer id;

    private String description;

    @NotNull(message = "Thoi gian không được để trống")
    private LocalDateTime timeIncident;
    private DeviceIndecentStatus status;

    @NotNull(message = "Thong tin quan ly không được để trống")
    private Integer managerId;

//    @NotNull(message = "Thong tin boooking không được để trống")
//    private Integer bookingId;

    @NotNull(message = "Thong tin nguoi vi pham không được để trống")
    private Integer incidentBy;
}
