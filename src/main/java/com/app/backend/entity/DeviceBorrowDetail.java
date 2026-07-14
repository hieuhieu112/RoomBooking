package com.app.backend.entity;

import com.app.backend.controller.DeviceBorrowDetailId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deviceborrowdetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBorrowDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "device_id",nullable = false)
    private Device device;


    private  String note;
}
