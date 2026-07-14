package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.DeviceCategory;

@Repository
public interface DeviceCategoryRepository extends JpaRepository<DeviceCategory, Integer> {}
