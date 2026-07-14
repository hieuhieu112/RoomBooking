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
import com.app.backend.entity.ManufacturerDevice;
import com.app.backend.repository.ManufacturerDeviceRepository;
import com.app.backend.service.ManufacturerDeviceService;

@Service
@Transactional
@AllArgsConstructor
public class ManufacturerDeviceServiceImpl implements ManufacturerDeviceService {
    private final ManufacturerDeviceRepository repo;

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
        return (entity);
    }

    @Override
    public ManufacturerDevice getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.MANUFACTOR_DEVICE_NOT_FOUND));
    }

    @Override
    public List<ManufacturerDevice> getAll() {
        return repo.findAll();
    }

    @Override
    public ManufacturerDevice update(Integer id, ManufacturerDeviceRequest request) {
        ManufacturerDevice entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
