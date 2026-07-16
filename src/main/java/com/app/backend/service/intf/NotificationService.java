package com.app.backend.service.intf;


import com.app.backend.entity.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    Notification create(Long userId,
                        String username,
                        String type,
                        String title,
                        String content,
                        Map<String, Object> metadata);
    List<Notification> getByUserId(Long userId);

    List<Notification> getByUsername(String username);
    void markAsRead(Long id, Integer updatedBy);

    long countUnread(Long userId);
}
