package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceType;

public interface DeviceTypeService {
    DeviceTypeResponse mapToResponse(DeviceType entity);
    DeviceType create(DeviceTypeRequest request);
    DeviceType getById(Integer id);
    List<DeviceType> getAll();
    DeviceType update(Integer id, DeviceTypeRequest request);
    void delete(Integer id);
}
