package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceCategory;

public interface DeviceCategoryService {
    DeviceCategoryResponse mapToResponse(DeviceCategory entity);
    DeviceCategory create(DeviceCategoryRequest request);
    DeviceCategory getById(Integer id);
    List<DeviceCategory> getAll();
    DeviceCategory update(Integer id, DeviceCategoryRequest request);
    void delete(Integer id);
}
