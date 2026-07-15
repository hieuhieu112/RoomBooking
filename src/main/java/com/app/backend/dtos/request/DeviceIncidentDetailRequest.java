package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentDetailRequest {

    @NotBlank(message = "Thong tin phieu phat không được để trống")
    private Integer deviceIncidentId;

    @NotBlank(message = "Thong tin thiet bi không được để trống")
    private Integer deviceId;
}
