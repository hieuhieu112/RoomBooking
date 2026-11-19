package com.app.backend.dtos.internal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceQuantityAvailable {
    private Integer deviceId;
    private String deviceName;
    private String trackingType;
    private Integer availableQuantity;
}
