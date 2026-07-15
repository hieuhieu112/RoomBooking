package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotBlank(message = "Ly do không được để trống")
    private String reason;
    @NotBlank(message = "Thoi gian bat dau không được để trống")
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    @NotBlank(message = "thong tin người thue không được để trống")
    private Integer userId;

    @NotBlank(message = "Thong tin phong không được để trống")
    private Integer roomId;
    private Integer approvedByUserId;
}
