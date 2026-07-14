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
import com.app.backend.entity.DeviceType;
import com.app.backend.repository.DeviceTypeRepository;
import com.app.backend.service.DeviceTypeService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceTypeServiceImpl implements DeviceTypeService {
    private final DeviceTypeRepository repo;


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
        return (entity);
    }

    @Override
    public DeviceType getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_TYPE_NOT_FOUND));
    }

    @Override
    public List<DeviceType> getAll() {
        return repo.findAll();
    }

    @Override
    public DeviceType update(Integer id, DeviceTypeRequest request) {
        DeviceType entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
