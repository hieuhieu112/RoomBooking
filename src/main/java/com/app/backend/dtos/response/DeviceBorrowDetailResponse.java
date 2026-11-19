package com.app.backend.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBorrowDetailResponse {
    private Long id;
    private Integer bookingId;
    private Integer deviceId;
    private String note;
    private Integer quantity;
//    private Integer serial;
}
