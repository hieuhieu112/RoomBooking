package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.DeviceIncident;

@Repository
public interface DeviceIncidentRepository extends JpaRepository<DeviceIncident, Integer> {}
