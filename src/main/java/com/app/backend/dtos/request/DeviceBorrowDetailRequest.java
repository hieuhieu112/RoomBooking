package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBorrowDetailRequest {
    private Integer id;

//    @NotBlank(message = "thong tin booking không được để trống")
//    private Integer bookingId;

    @NotNull(message = "Thong tin thiet bi không được để trống")
    private Integer deviceId;

    private Integer quantity;
}
