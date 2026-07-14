package com.app.backend.controller;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBorrowDetailId implements Serializable {
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "device_id")
    private Integer deviceId;
}
