package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGroupRequest {

    @NotBlank(message = "Ten không được để trống")
    private String name;

//    @NotEmpty(message = "Danh sach user không được để trống")
    private List<Integer> listUser;

//    @NotEmpty(message = "Danh sách phòng không được để trống")
    private List<Integer> listRoom;
}
