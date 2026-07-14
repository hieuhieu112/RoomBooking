package com.app.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    public void set(
            String key,
            Object value,
            Duration ttl
    ) {

        try {

            String json =
                    objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue()
                    .set(key, json, ttl);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(
            String key,
            Class<T> clazz
    ) {

        try {

            String json =
                    redisTemplate.opsForValue().get(key);

            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, clazz);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void expire(
            String key,
            Duration ttl
    ) {
        redisTemplate.expire(key, ttl);
    }

    public void deleteByPattern(String pattern) {

        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return;
        }

        redisTemplate.delete(keys);
    }
}
