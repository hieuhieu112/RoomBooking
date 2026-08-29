package com.app.backend.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGroupResponse {
    private Integer id;
    private String name;
    private List<Integer> listUser;
    private List<Integer> listRoom;
//    private List<Integer> userIDs;
//    private List<Integer> roomIDs;
}
