package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequest {
    private Integer id;

    @NotBlank(message = "Ten không được để trống")
    private String name;
}
