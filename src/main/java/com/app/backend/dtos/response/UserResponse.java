package com.app.backend.dtos.response;

import com.app.backend.entity.ManagerGroup;
import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer incidentCount;
//    private String pass;
    private String status;
    private String username;
//    private Integer managerGroupId;
    private List<String> roles;

    public static UserResponse convertFromEntity(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .status(user.getStatus().name())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
