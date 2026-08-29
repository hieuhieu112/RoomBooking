package com.app.backend.repository;

import com.app.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.RoomType;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    @Query(value = "Select r FROM RoomType r  WHERE :search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    List<RoomType> getAllByFilter(
            @Param("search")  String search
    );
}
