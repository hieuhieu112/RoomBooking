package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.ManagerGroup;

@Repository
public interface ManagerGroupRepository extends JpaRepository<ManagerGroup, Integer> {}
