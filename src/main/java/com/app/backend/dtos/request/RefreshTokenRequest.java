package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "Thoi gian het han không được để trống")
    private LocalDateTime expiryDate;
    private String token;
    private Integer userId;
}
