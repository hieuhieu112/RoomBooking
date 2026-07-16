package com.app.backend.service.impl;

import com.app.backend.entity.Notification;
import com.app.backend.repository.NotificationRepository;
import com.app.backend.service.intf.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceIml implements NotificationService {

    private final NotificationRepository repository;


    @Override
    @Transactional
    public Notification create(Long userId, String username, String type, String title, String content, Map<String, Object> metadata) {
        Notification n = Notification.builder()
                .userId(userId)
                .username(username)
                .type(type)
                .title(title)
                .content(content)
                .metadata(metadata)
                .isRead(false)
                .build();

        return repository.save(n);
    }

    @Override
    public List<Notification> getByUserId(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getByUsername(String username) {
        return repository.findTop20ByUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public void markAsRead(Long id, Integer updatedBy) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setIsRead(true);
        n.setReadAt(LocalDateTime.now());
        n.setModifyBy(updatedBy);
    }

    @Override
    public long countUnread(Long userId) {
        return repository.countByUserIdAndIsReadFalse(userId);
    }
}
