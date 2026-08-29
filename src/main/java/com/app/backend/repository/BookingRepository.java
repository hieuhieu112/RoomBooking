package com.app.backend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = """
    SELECT *
    FROM booking b
    WHERE b.room_id = :roomId
      AND b.actual_end_time >= :startTime
      AND b.start_time <= :endTime
    """, nativeQuery = true)
    List<Booking> findOverlappingBookings(
            @Param("roomId") Integer roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query(value = """
    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
    FROM Booking b
    WHERE b.room_id = :roomId
      AND b.actual_end_time >= :startTime
      AND b.start_time <= :endTime
    """, nativeQuery = true)
    Boolean existsOverlappingBookings(
            @Param("roomId") Integer roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );


    @Query(value = """
    SELECT *
    FROM booking b
    WHERE b.room_id IN (:roomIds)
    """, nativeQuery = true)
    List<Booking> findByRoomIds(
            @Param("roomIds") List<Integer> roomIds
    );
}
