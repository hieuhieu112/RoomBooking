package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceIncidentDetail;
import com.app.backend.repository.DeviceIncidentDetailRepository;
import com.app.backend.service.DeviceIncidentDetailService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceIncidentDetailServiceImpl implements DeviceIncidentDetailService {
    private final DeviceIncidentDetailRepository repo;
    private final DeviceServiceImpl deviceService;
    private final DeviceIncidentServiceImpl deviceIncidentService;

    public DeviceIncidentDetailResponse mapToResponse(DeviceIncidentDetail entity) {
        DeviceIncidentDetailResponse resp = new DeviceIncidentDetailResponse();
        resp.setId(entity.getId());
        resp.setDeviceIncidentId(entity.getDeviceIncident().getId());
        resp.setDeviceId(entity.getDevice().getId());
        return resp;
    }

    @Override
    public DeviceIncidentDetail create(DeviceIncidentDetailRequest request) {
        DeviceIncidentDetail entity = new DeviceIncidentDetail();
        entity.setDeviceIncident(deviceIncidentService.getById(request.getDeviceIncidentId()));
        entity.setDevice(deviceService.getById(request.getDeviceId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public DeviceIncidentDetail getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_INCIENT_NOT_FOUND));
    }

    @Override
    public List<DeviceIncidentDetail> getAll() {
        return repo.findAll();
    }

    @Override
    public DeviceIncidentDetail update(Long id, DeviceIncidentDetailRequest request) {
        DeviceIncidentDetail entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setDeviceIncident(deviceIncidentService.getById(request.getDeviceIncidentId()));
        entity.setDevice(deviceService.getById(request.getDeviceId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }
}
