package com.app.backend.repository;

import com.app.backend.dtos.internal.DeviceQuantityAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.DeviceBorrowDetail;

import java.util.List;

@Repository
public interface DeviceBorrowDetailRepository extends JpaRepository<DeviceBorrowDetail, Long> {
    @Query(value = "select * from device_borrow_detail where booking_id = :p_booking_id", nativeQuery = true)
    List<DeviceBorrowDetail> findAddByBookingId(@Param("p_booking_id") Integer p_booking_id);

    @Query(value = "delete * from device_borrow_detail where booking_id = :p_booking_id", nativeQuery = true)
    void deleteAllByBookingId(@Param("p_booking_id") Integer p_booking_id);
}
