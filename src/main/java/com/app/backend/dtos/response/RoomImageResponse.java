package com.app.backend.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomImageResponse {
    private Integer id;
    private String url;
    private Integer displayOrder;
    private Integer roomId;
    private String originalName;
}
