package com.app.backend.dtos.response;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DataResponse<T> {
    private String statusCode;

    private String message;

    private String path;

    private String redirect;

    private LocalDateTime timestamp;

    private T data;
}
