package com.app.backend.dtos.request;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.entity.enumm.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Integer id;
    @NotBlank(message = "Ly do không được để trống")
    private String reason;
    @NotNull(message = "Thoi gian bat dau không được để trống")
    private LocalDateTime startTime;
    @NotNull(message = "Thoi gian bat dau không được để trống")
    private LocalDateTime endTime;
    @NotNull(message = "Thong tin phong không được để trống")
    private Integer roomId;

    private List<DeviceBorrowDetailRequest> deviceBorrowDetail;

//    private BookingStatus status;

//    @NotBlank(message = "thong tin người thue không được để trống")
//    private Integer userId;
//    private Integer approvedByUserId;


}
