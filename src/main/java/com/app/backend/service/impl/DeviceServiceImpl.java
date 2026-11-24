package com.app.backend.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.app.backend.constant.RedisKey;
import com.app.backend.dtos.internal.DeviceQuantityAvailable;
import com.app.backend.entity.RoomType;
import com.app.backend.entity.enumm.DeviceTrackingType;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Device;
import com.app.backend.repository.DeviceRepository;
import com.app.backend.service.intf.DeviceService;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository repo;
    private final DeviceModelServiceImpl deviceModelService;
    private final DeviceCategoryServiceImpl deviceCategoryService;
    private final RoomServiceImpl roomService;
    private final CacheService cacheService;

    private static final Duration DEVICE_CACHE_TTL = Duration.ofMinutes(15);
    public DeviceResponse mapToResponse(Device entity) {
        DeviceResponse resp = new DeviceResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setStatus(entity.getStatus().toString());
        resp.setDeviceCategoryId(entity.getDeviceCategory().getId());
        resp.setDeviceModelId(entity.getDeviceModel().getId());
        resp.setSerial(entity.getSerial());
        resp.setQuantity(entity.getQuantity());
        resp.setTrackingType(entity.getTrackingType());
//        resp.setRoomId(entity.getRoomId());
        return resp;
    }

    private void checkValidData(DeviceRequest request){
        if(repo.existsBySerial(request.getSerial())){
            throw  new CommonException(ErrorCode.VALIDATION);
        }
    }

    public List<DeviceQuantityAvailable> getAllQuantityAvailable(
            LocalDateTime startTime, List<Integer> ids, LocalDateTime endTime, Integer idException
    ) {
        List<DeviceQuantityAvailable> a = repo.findAllQuantityAvailable(
                startTime == null ? null : startTime.toString(),
                ids,
                endTime == null ? null : endTime.toString(),
                idException
        );
        return a;
    }

    @Override
    public Device create(DeviceRequest request) {
        checkValidData(request);
        Device entity = new Device();
        entity.setName(request.getName());
        entity.setStatus(request.getStatus());
        entity.setDeviceCategory(deviceCategoryService.getById(request.getDeviceCategoryId()));
        entity.setDeviceModel(deviceModelService.getById(request.getDeviceModelId()));
        entity.setQuantity(request.getQuantity());
        entity.setTrackingType(request.getTrackingType());
        entity.setSerial(request.getSerial());
        if(request.getTrackingType().equals(DeviceTrackingType.QUANTITY) && request.getSerial().isEmpty()){
            entity.generateSerial();
            entity.setQuantity(1);
        }
//        entity.setRoom(roomService.getById(request.getRoomId()));
        entity = repo.save(entity);

        evictTopicCache(null);
        return (entity);
    }

    private void evictTopicCache(Integer topicId) {
        try {
            cacheService.evict(RedisKey.deviceAll());
            if (topicId != null) {
                cacheService.evict(RedisKey.deviceById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public Device getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.deviceById(id),
                Device.class,
                DEVICE_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_NOT_FOUND))
        );
    };


    @Override
    public List<Device> getByIdsForUpdate(List<Integer> ids) {
        return repo.findByIdForUpdate(ids);
    }

    @Override
    public List<Device> getAll() {
        Device[] items = cacheService.getOrLoad(
                RedisKey.deviceAll(),
                Device[].class,
                DEVICE_CACHE_TTL,
                () -> repo.findAll().toArray(new Device[0])
        );
        return Arrays.stream(items).toList();
    }

    @Override
    public Device update(Integer id, DeviceRequest request) {

        checkValidData(request);
        Device entity = getById(id);
        entity.setName(request.getName());
        entity.setStatus(request.getStatus());
        entity.setDeviceCategory(deviceCategoryService.getById(request.getDeviceCategoryId()));
        entity.setDeviceModel(deviceModelService.getById(request.getDeviceModelId()));
        entity.setQuantity(request.getQuantity());
        entity.setTrackingType(request.getTrackingType());
        entity.setSerial(request.getSerial());
        if(request.getTrackingType().equals(DeviceTrackingType.QUANTITY) && request.getSerial().isEmpty()){
            entity.generateSerial();
        }
//        entity.setRoomId(request.getRoomId());
        entity = repo.save(entity);
        evictTopicCache(id);
        return (entity);
    }

    @Override
    public void delete(Integer id) {
        evictTopicCache(id);
        repo.deleteById(id);
    }
}
