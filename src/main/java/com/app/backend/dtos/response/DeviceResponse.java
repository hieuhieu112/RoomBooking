package com.app.backend.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private Integer id;
    private String name;
    private String status;
//    private Integer deviceCategoryId;
//    private Integer deviceModelId;
//    private Integer roomId;
}
