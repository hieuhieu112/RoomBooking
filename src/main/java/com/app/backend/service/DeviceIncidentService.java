package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceIncident;

public interface DeviceIncidentService {
    DeviceIncidentResponse mapToResponse(DeviceIncident entity);
    DeviceIncident create(DeviceIncidentRequest request);
    DeviceIncident getById(Integer id);
    List<DeviceIncident> getAll();
    DeviceIncident update(Integer id, DeviceIncidentRequest request);
    void delete(Integer id);
}
