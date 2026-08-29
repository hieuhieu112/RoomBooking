package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModelRequest {
    private Integer id;

    @NotBlank(message = "Ten không được để trống")
    private String name;
    private Integer deviceTypeId;
    private Integer manufacturerDeviceId;
    private String specifications;
}
