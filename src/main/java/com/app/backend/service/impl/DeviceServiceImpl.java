package com.app.backend.service.impl;

import java.util.List;

import com.app.backend.entity.enumm.DeviceTrackingType;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
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

    public DeviceResponse mapToResponse(Device entity) {
        DeviceResponse resp = new DeviceResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setStatus(entity.getStatus().toString());
//        resp.setDeviceCategoryId(entity.getDeviceCategory().);
//        resp.setDeviceModelId(entity.getDeviceModelId());
//        resp.setRoomId(entity.getRoomId());
        return resp;
    }

    private void checkValidData(DeviceRequest request){
        if(repo.existsBySerial(request.getSerial())){
            throw  new CommonException(ErrorCode.VALIDATION);
        }
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
        }
//        entity.setRoom(roomService.getById(request.getRoomId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public Device getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_NOT_FOUND));
    }

    @Override
    public List<Device> getAll() {
        return repo.findAll();
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
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
