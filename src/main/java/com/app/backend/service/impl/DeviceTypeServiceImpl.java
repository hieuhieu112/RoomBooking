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
import com.app.backend.entity.DeviceType;
import com.app.backend.repository.DeviceTypeRepository;
import com.app.backend.service.intf.DeviceTypeService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceTypeServiceImpl implements DeviceTypeService {
    private final DeviceTypeRepository repo;
    private final CacheService cacheService;

    private static final Duration DEVICETYPE_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;


    public DeviceTypeResponse mapToResponse(DeviceType entity) {
        DeviceTypeResponse resp = new DeviceTypeResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public DeviceType create(DeviceTypeRequest request) {
        DeviceType entity = new DeviceType();
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public DeviceType getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.deviceTypeById(id),
                DeviceType.class,
                DEVICETYPE_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_TYPE_NOT_FOUND))
        );
    }

    @Override
    public List<DeviceType> getAll() {
        DeviceType[] items = cacheService.getOrLoad(
                RedisKey.deviceTypeAll(),
                DeviceType[].class,
                DEVICETYPE_CACHE_TTL,
                () -> repo.findAll().toArray(new DeviceType[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public DeviceType update(Integer id, DeviceTypeRequest request) {
        DeviceType entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.deviceTypeAll());
            if (topicId != null) {
                redisService.delete(RedisKey.deviceTypeById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
