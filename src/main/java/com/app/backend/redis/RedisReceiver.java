package com.app.backend.redis;

import com.app.backend.service.impl.NotificationServiceIml;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisReceiver {

    private  final NotificationServiceIml notificationService;
    private final ObjectMapper objectMapper;


    public void receiveMessage(String json) throws JsonProcessingException {
        NotificationEvent message =
                objectMapper.readValue(json, NotificationEvent.class);
//        notificationService.create(message.getUserId(), message.getUsername(), message.getType(), message.getTitle(), message.getContent(), message.getMetadata());
        notificationService.createFromEvent(message);
    }
}
