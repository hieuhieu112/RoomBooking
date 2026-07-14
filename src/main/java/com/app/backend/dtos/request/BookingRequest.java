package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.BookingStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private Integer userId;
    private Integer roomId;
    private Integer approvedByUserId;
}
