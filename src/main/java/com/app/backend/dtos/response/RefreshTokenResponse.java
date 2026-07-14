package com.app.backend.dtos.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private Integer id;
    private LocalDateTime expiryDate;
    private String token;
    private Integer userId;
}
