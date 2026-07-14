package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.ManufacturerDevice;

public interface ManufacturerDeviceService {
    ManufacturerDeviceResponse mapToResponse(ManufacturerDevice entity);
    ManufacturerDevice create(ManufacturerDeviceRequest request);
    ManufacturerDevice getById(Integer id);
    List<ManufacturerDevice> getAll();
    ManufacturerDevice update(Integer id, ManufacturerDeviceRequest request);
    void delete(Integer id);
}
