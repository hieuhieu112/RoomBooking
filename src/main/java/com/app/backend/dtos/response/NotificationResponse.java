package com.app.backend.dtos.response;

import com.app.backend.entity.enumm.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Integer userId;
    private String username;
    private NotificationType type;
    private String title;
    private String content;
    private Map<String, Object> metadata;
    private Boolean isRead;
    private LocalDateTime readAt;
    private Long referenceId;
    private String url;
}
