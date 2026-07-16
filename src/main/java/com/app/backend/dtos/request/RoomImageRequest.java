package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomImageRequest {

    @NotBlank(message = "url không được để trống")
    private String url;

//    @NotBlank(message = "Thong tin phong không được để trống")
//    private Integer roomId;
}
