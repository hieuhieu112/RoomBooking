package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {}
