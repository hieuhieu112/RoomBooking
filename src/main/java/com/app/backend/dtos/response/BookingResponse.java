package com.app.backend.dtos.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Integer id;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer userId;
    private Integer roomId;
    private Integer approvedByUserId;
}
