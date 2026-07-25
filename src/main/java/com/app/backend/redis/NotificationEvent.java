package com.app.backend.redis;

import com.app.backend.entity.enumm.NotificationType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private Integer userId;

    private String username;

    private NotificationType type;

    private String title;

    private String content;

    Map<String, Object> metadata;
}
