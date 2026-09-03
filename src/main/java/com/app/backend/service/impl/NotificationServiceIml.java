package com.app.backend.service.impl;

import com.app.backend.dtos.internal.AuthContext;
import com.app.backend.dtos.response.NotificationResponse;
import com.app.backend.entity.User;
import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.redis.NotificationEvent;
import com.app.backend.entity.Notification;
import com.app.backend.repository.NotificationRepository;
import com.app.backend.service.AuthContextService;
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
    public NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();

        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setUsername(notification.getUsername());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setMetadata(notification.getMetadata());
        response.setIsRead(notification.getIsRead());
        response.setReadAt(notification.getReadAt());
        response.setReferenceId(notification.getReferenceId());

        return response;
    }

    @Override
    @Transactional
    public Notification create(
            Integer userId, String username,
            NotificationType type, String title,
            String content, Map<String, Object> metadata,
            Long referenceId
    ) {
        Notification n = Notification.builder()
                .userId(userId)
                .username(username)
                .type(type)
                .title(title)
                .content(content)
                .metadata(metadata)
                .isRead(false)
                .referenceId(referenceId)
                .build();

        return repository.save(n);
    }

    @Override
    public Notification createFromEvent(NotificationEvent event) {
        Notification n = Notification.builder()
                .userId(event.getUserId())
                .username(event.getUsername())
                .type(event.getType())
                .title(event.getTitle())
                .content(event.getContent())
                .metadata(event.getMetadata())
                .referenceId(event.getReferenceId())
                .isRead(false)
                .build();
        n.setCreatedBy(event.getUserId());
        n.setModifyBy(event.getUserId());

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
    public Notification markAsRead(Long id) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setIsRead(true);
        n.setReadAt(LocalDateTime.now());

        AuthContext context = AuthContextService.getContext();
        n.setModifyBy(context.getUserId());

        return repository.save(n);
    }

    public String getPath(Notification n){
        String url = n.getType().getUrl().replace("{id}", n.getReferenceId().toString());
        return url;
    }

    @Override
    public long countUnread() {
        AuthContext context = AuthContextService.getContext();
        return repository.countByUserIdAndIsReadFalse(context.getUserId().longValue());
    }

    @Override
    public List<Notification> getByUser() {
        AuthContext context = AuthContextService.getContext();

        return getByUsername(context.getUsername());
    }
}
