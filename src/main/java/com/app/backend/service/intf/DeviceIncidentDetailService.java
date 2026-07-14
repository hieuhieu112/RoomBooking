package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceIncidentDetail;

public interface DeviceIncidentDetailService {
    DeviceIncidentDetailResponse mapToResponse(DeviceIncidentDetail entity);
    DeviceIncidentDetail create(DeviceIncidentDetailRequest request);
    DeviceIncidentDetail getById(Long id);
    List<DeviceIncidentDetail> getAll();
    DeviceIncidentDetail update(Long id, DeviceIncidentDetailRequest request);
    void delete(Long id);
}
