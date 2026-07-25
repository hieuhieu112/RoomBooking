package com.app.backend.service.intf;


import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.redis.NotificationEvent;
import com.app.backend.entity.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    Notification create(Integer userId,
                        String username,
                        NotificationType type,
                        String title,
                        String content,
                        Map<String, Object> metadata);
    Notification createFromEvent(NotificationEvent event);
    List<Notification> getByUserId(Long userId);

    List<Notification> getByUsername(String username);
    void markAsRead(Long id, Integer updatedBy);

    long countUnread(Long userId);
}
