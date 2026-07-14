package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {}
