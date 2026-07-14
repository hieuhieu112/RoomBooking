package com.app.backend.dtos.request;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private LocalDateTime expiryDate;
    private String token;
    private Integer userId;
}
