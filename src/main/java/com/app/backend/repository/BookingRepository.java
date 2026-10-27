package com.app.backend.repository;

import com.app.backend.entity.Room;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Booking> findByRoomIds(
            @Param("roomIds") List<Integer> roomIds,
            Pageable page
    );

    @Query(
            value = """
    SELECT *
    FROM booking b
    WHERE b.user_id = :userid
    """,
            nativeQuery = true
            ,
            countQuery = "SELECT COUNT(*) " +
                    "    FROM booking b" +
                    "    WHERE b.user_id = :userid"
    )
    Page<Booking> findBooingByUserId(
            @Param("userid") Integer userid,
            Pageable page
    );


    @Query(
            value = "SELECT * FROM booking_filter(CAST(:p_start AS timestamp),:p_status,:p_room_ids,CAST(:p_end_time AS timestamp))",
            countQuery = "SELECT COUNT(*) FROM booking_filter(CAST(:p_start AS timestamp),:p_status,:p_room_ids,CAST(:p_end_time AS timestamp))",
            nativeQuery = true
    )
    Page<Booking> findBookingFilter(
            @Param("p_start") String  p_start,
            @Param("p_status") Integer p_status,
            @Param("p_room_ids") List<Integer> p_room_ids,
            @Param("p_end_time") String  p_end_time,
            Pageable pageable
    );
}
