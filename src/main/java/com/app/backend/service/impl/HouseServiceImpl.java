package com.app.backend.service.impl;

import java.util.List;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import com.app.backend.constant.RedisKey;
import com.app.backend.service.CacheService;
import com.app.backend.service.RedisService;
import java.time.Duration;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.House;
import com.app.backend.repository.HouseRepository;
import com.app.backend.service.intf.HouseService;

@Service
@Transactional
@AllArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository repo;
    private final CacheService cacheService;

    private static final Duration HOUSE_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;

    public HouseResponse mapToResponse(House entity) {
        HouseResponse resp = new HouseResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public House create(HouseRequest request) {
        House entity = new House();
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public House getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.houseById(id),
                House.class,
                HOUSE_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.HOUSE_NOT_FOUND))
        );
    }

    @Override
    public List<House> getAll() {
        House[] items = cacheService.getOrLoad(
                RedisKey.houseAll(),
                House[].class,
                HOUSE_CACHE_TTL,
                () -> repo.findAll().toArray(new House[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public House update(Integer id, HouseRequest request) {
        House entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.houseAll());
            if (topicId != null) {
                redisService.delete(RedisKey.houseById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
