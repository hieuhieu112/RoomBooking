package com.app.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.Room;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Boolean existsByName(String name);

    @Query(value = "Select r FROM Room r " +
            "WHERE (:houseId IS NULL OR r.house.id = :houseId)" +
            "AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId)" +
            "AND " +
            "( " +
                ":search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(r.location) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ")"
    )
    Page<Room> getAllByFilter(
            @Param("search")  String search,
            @Param("houseId") Integer houseId,
            @Param("roomTypeId")  Integer roomTypeId,
            Pageable pageable
    );


    @Query(
            value = "SELECT * FROM available_rooms(CAST(:p_start AS timestamp),CAST(:p_end_time AS timestamp)  )",
            countQuery = "SELECT COUNT(*) FROM available_rooms(CAST(:p_start AS timestamp),CAST(:p_end_time AS timestamp))",
            nativeQuery = true
    )
    Page<Room> findRoomAvalable(
            @Param("p_start") String  p_start,
            @Param("p_end_time") String  p_end_time,
            Pageable pageable
    );
}
