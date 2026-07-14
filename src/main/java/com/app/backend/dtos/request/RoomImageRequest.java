package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomImageRequest {
    private String url;
    private Integer roomId;
}
