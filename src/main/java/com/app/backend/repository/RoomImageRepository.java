package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.RoomImage;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {}
