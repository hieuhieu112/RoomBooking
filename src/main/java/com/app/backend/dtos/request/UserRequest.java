package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private Integer incidentCount;
    private String pass;
    private String status;
    private String username;
    private Integer managerGroupId;
}
