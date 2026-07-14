package com.app.backend.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Integer id;
    private String name;
    private String location;
    private Integer capacity;
    private Integer roomTypeId;
    private Integer houseId;
    private List<String> image;
    private Integer managerGroupId;
}
