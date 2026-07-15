package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;
    private String location;
    private Integer capacity;
    private Integer roomTypeId;
    private Integer houseId;
    private String image;
    private Integer managerGroupId;
}
