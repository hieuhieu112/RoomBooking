package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.DeviceIncidentDetail;

@Repository
public interface DeviceIncidentDetailRepository extends JpaRepository<DeviceIncidentDetail, Long> {}
