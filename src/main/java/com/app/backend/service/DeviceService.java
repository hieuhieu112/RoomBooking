package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Device;

public interface DeviceService {
    DeviceResponse mapToResponse(Device entity);
    Device create(DeviceRequest request);
    Device getById(Integer id);
    List<Device> getAll();
    Device update(Integer id, DeviceRequest request);
    void delete(Integer id);
}
