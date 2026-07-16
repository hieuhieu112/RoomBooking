package com.app.backend.repository;

import com.app.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUsernameOrderByCreatedAtDesc(String username);
    List<Notification> findTop20ByUsernameOrderByCreatedAtDesc(String username);
    long countByUserIdAndIsReadFalse(Long userId);
}
