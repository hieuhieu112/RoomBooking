package com.app.backend.service.intf;


import com.app.backend.dtos.response.NotificationResponse;
import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.redis.NotificationEvent;
import com.app.backend.entity.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    NotificationResponse mapToResponse(Notification notification);

    Notification create(Integer userId,
                        String username,
                        NotificationType type,
                        String title,
                        String content,
                        Map<String, Object> metadata,
                        Long referenceId
                        );
    Notification createFromEvent(NotificationEvent event);
    List<Notification> getByUserId(Long userId);

    List<Notification> getByUsername(String username);
    Notification markAsRead(Long id);

    long countUnread();

    List<Notification> getByUser();
}
