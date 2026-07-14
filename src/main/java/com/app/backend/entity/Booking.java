package com.app.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.app.backend.entity.enumm.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime = LocalDateTime.now();

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime = LocalDateTime.now();

    @Column(nullable = false)
    private BookingStatus status =BookingStatus.INPROCESS;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userUsing;

    @ManyToOne
    @JoinColumn(name = "room_id" , nullable = false)
    private Room room;


    @ManyToOne
    @JoinColumn(name = "approved_by_user_id", nullable = false)
    private User userApproved;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceBorrowDetail> deviceBorrowDetail;

}
