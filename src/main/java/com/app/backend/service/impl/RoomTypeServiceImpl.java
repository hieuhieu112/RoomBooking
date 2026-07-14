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
import com.app.backend.entity.RoomType;
import com.app.backend.repository.RoomTypeRepository;
import com.app.backend.service.intf.RoomTypeService;

@Service
@Transactional
@AllArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository repo;
    private final CacheService cacheService;

    private static final Duration ROOMTYPE_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;

    public RoomTypeResponse mapToResponse(RoomType entity) {
        RoomTypeResponse resp = new RoomTypeResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public RoomType create(RoomTypeRequest request) {
        RoomType entity = new RoomType();
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public RoomType getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.roomTypeById(id),
                RoomType.class,
                ROOMTYPE_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROOM_TYPE_NOT_FOUND))
        );
    }

    @Override
    public List<RoomType> getAll() {
        RoomType[] items = cacheService.getOrLoad(
                RedisKey.roomTypeAll(),
                RoomType[].class,
                ROOMTYPE_CACHE_TTL,
                () -> repo.findAll().toArray(new RoomType[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public RoomType update(Integer id, RoomTypeRequest request) {
        RoomType entity = getById(id);
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.roomTypeAll());
            if (topicId != null) {
                redisService.delete(RedisKey.roomTypeById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
