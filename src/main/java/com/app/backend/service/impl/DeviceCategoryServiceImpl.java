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
import com.app.backend.entity.DeviceCategory;
import com.app.backend.repository.DeviceCategoryRepository;
import com.app.backend.service.DeviceCategoryService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceCategoryServiceImpl implements DeviceCategoryService {
    private final DeviceCategoryRepository repo;

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
        return (entity);
    }

    @Override
    public DeviceCategory getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_CATEGORY_NOT_FOUND));
    }

    @Override
    public List<DeviceCategory> getAll() {
        return repo.findAll();
    }

    @Override
    public DeviceCategory update(Integer id, DeviceCategoryRequest request) {
        DeviceCategory entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
