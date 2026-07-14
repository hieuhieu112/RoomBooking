package com.app.backend.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBorrowDetailRequest {

    private Integer bookingId;
    private Integer deviceId;


    private String note;
}
