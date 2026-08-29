package com.app.backend.repository;

import com.app.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.House;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {
    @Query(value = "Select r FROM House r  WHERE :search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    List<House> getAllByFilter(
            @Param("search")  String search
    );
}
