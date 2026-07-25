package com.app.backend.redis;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationPublisher {

    private final RedisConfig redisChannel;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(NotificationEvent message)  {
        try
        {

            String json = objectMapper.writeValueAsString(message);

            redisTemplate.convertAndSend(redisChannel.redisChannel(), json);
        }
        catch (JsonProcessingException exception){
            throw  new CommonException(ErrorCode.VALIDATION);
        }

    }
}
