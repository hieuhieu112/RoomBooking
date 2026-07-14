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
import com.app.backend.entity.ManufacturerDevice;
import com.app.backend.repository.ManufacturerDeviceRepository;
import com.app.backend.service.intf.ManufacturerDeviceService;

@Service
@Transactional
@AllArgsConstructor
public class ManufacturerDeviceServiceImpl implements ManufacturerDeviceService {
    private final ManufacturerDeviceRepository repo;
    private final CacheService cacheService;

    private static final Duration MANUFACTURERDEVICE_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;

    public ManufacturerDeviceResponse mapToResponse(ManufacturerDevice entity) {
        ManufacturerDeviceResponse resp = new ManufacturerDeviceResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public ManufacturerDevice create(ManufacturerDeviceRequest request) {
        ManufacturerDevice entity = new ManufacturerDevice();
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public ManufacturerDevice getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.manufacturerById(id),
                ManufacturerDevice.class,
                MANUFACTURERDEVICE_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.MANUFACTOR_DEVICE_NOT_FOUND))
        );
    }

    @Override
    public List<ManufacturerDevice> getAll() {
        ManufacturerDevice[] items = cacheService.getOrLoad(
                RedisKey.manufacturerAll(),
                ManufacturerDevice[].class,
                MANUFACTURERDEVICE_CACHE_TTL,
                () -> repo.findAll().toArray(new ManufacturerDevice[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public ManufacturerDevice update(Integer id, ManufacturerDeviceRequest request) {
        ManufacturerDevice entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.manufacturerAll());
            if (topicId != null) {
                redisService.delete(RedisKey.manufacturerById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
