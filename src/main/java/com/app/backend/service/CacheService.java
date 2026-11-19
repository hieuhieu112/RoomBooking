package com.app.backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisService redisService;

    public <T> T getOrLoad(
            String key,
            Class<T> type,
            Duration ttl,
            Supplier<T> loader
    ) {

        T cached = getCache(
                key,
                type
        );

        if (cached != null) {
            return cached;
        }

        T value = loader.get();

        if (value != null) {
            setCache(
                    key,
                    value,
                    ttl
            );
        }

        return value;
    }

    public <T> T getCache(
            String key,
            Class<T> type
    ) {

        return redisService.get(
                key,
                type
        );
    }

    public void setCache(
            String key,
            Object value,
            Duration ttl
    ) {

        redisService.set(
                key,
                value,
                ttl
        );
    }

    public void evictByPatten(String patten) {
        redisService.deleteByPattern(patten);
    }

    public void evict(String key) {
        redisService.delete(key);
//        try {
//            redisService.delete(key);
//        } catch (Exception ignored) {
//        }
    }
}
