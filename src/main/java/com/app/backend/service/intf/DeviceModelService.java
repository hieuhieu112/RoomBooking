package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceModel;

public interface DeviceModelService {
    DeviceModelResponse mapToResponse(DeviceModel entity);
    DeviceModel create(DeviceModelRequest request);
    DeviceModel getById(Integer id);
    List<DeviceModel> getAll();
    DeviceModel update(Integer id, DeviceModelRequest request);
    void delete(Integer id);
}
