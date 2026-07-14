package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String name;
    private String location;
    private Integer capacity;
    private Integer roomTypeId;
    private Integer houseId;
    private String image;
    private Integer managerGroupId;
}
