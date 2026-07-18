package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.RoomImage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
    List<RoomImage> findByRoomId(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoomImage r WHERE r.room.id = :id")
    void deleteByRoomId(@Param("id") Integer id);
}
