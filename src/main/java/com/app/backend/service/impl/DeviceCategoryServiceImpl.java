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
import com.app.backend.entity.DeviceCategory;
import com.app.backend.repository.DeviceCategoryRepository;
import com.app.backend.service.intf.DeviceCategoryService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceCategoryServiceImpl implements DeviceCategoryService {
    private final DeviceCategoryRepository repo;
    private final CacheService cacheService;

    private static final Duration DEVICECATEGORY_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;

    public DeviceCategoryResponse mapToResponse(DeviceCategory entity) {
        DeviceCategoryResponse resp = new DeviceCategoryResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public DeviceCategory create(DeviceCategoryRequest request) {
        DeviceCategory entity = new DeviceCategory();
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public DeviceCategory getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.deviceCategoryById(id),
                DeviceCategory.class,
                DEVICECATEGORY_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_CATEGORY_NOT_FOUND))
        );
    }

    @Override
    public List<DeviceCategory> getAll() {
        DeviceCategory[] items = cacheService.getOrLoad(
                RedisKey.deviceCategoryAll(),
                DeviceCategory[].class,
                DEVICECATEGORY_CACHE_TTL,
                () -> repo.findAll().toArray(new DeviceCategory[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public DeviceCategory update(Integer id, DeviceCategoryRequest request) {
        DeviceCategory entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.deviceCategoryAll());
            if (topicId != null) {
                redisService.delete(RedisKey.deviceCategoryById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
