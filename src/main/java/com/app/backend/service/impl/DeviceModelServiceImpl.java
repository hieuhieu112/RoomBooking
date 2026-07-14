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
import com.app.backend.entity.DeviceModel;
import com.app.backend.repository.DeviceModelRepository;
import com.app.backend.service.intf.DeviceModelService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceModelServiceImpl implements DeviceModelService {
    private final DeviceModelRepository repo;
    private final CacheService cacheService;

    private static final Duration DEVICEMODEL_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;
    private final DeviceTypeServiceImpl deviceTypeService;
    private final ManufacturerDeviceServiceImpl manufacturerDeviceService;


    public DeviceModelResponse mapToResponse(DeviceModel entity) {
        DeviceModelResponse resp = new DeviceModelResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setDeviceTypeId(entity.getDeviceType().getId());
        resp.setManufacturerDeviceId(entity.getManufacturerDevice().getId());
        resp.setSpecifications(entity.getSpecifications());
        return resp;
    }

    @Override
    public DeviceModel create(DeviceModelRequest request) {
        DeviceModel entity = new DeviceModel();
        entity.setName(request.getName());
        entity.setDeviceType(deviceTypeService.getById(request.getDeviceTypeId()));
        entity.setManufacturerDevice(manufacturerDeviceService.getById(request.getManufacturerDeviceId()));
        entity.setSpecifications(request.getSpecifications());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public DeviceModel getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.deviceModelById(id),
                DeviceModel.class,
                DEVICEMODEL_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_MODEL_NOT_FOUND))
        );
    }

    @Override
    public List<DeviceModel> getAll() {
        DeviceModel[] items = cacheService.getOrLoad(
                RedisKey.deviceModelAll(),
                DeviceModel[].class,
                DEVICEMODEL_CACHE_TTL,
                () -> repo.findAll().toArray(new DeviceModel[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public DeviceModel update(Integer id, DeviceModelRequest request) {
        DeviceModel entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity.setDeviceType(deviceTypeService.getById(request.getDeviceTypeId()));
        entity.setManufacturerDevice(manufacturerDeviceService.getById(request.getManufacturerDeviceId()));
        entity.setSpecifications(request.getSpecifications());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.deviceModelAll());
            if (topicId != null) {
                redisService.delete(RedisKey.deviceModelById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
