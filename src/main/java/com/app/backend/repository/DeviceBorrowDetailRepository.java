package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.DeviceBorrowDetail;

@Repository
public interface DeviceBorrowDetailRepository extends JpaRepository<DeviceBorrowDetail, Long> {}
