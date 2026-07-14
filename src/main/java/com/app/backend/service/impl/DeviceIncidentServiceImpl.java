package com.app.backend.service.impl;

import java.util.List;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceIncident;
import com.app.backend.repository.DeviceIncidentRepository;
import com.app.backend.service.intf.DeviceIncidentService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceIncidentServiceImpl implements DeviceIncidentService {
    private final DeviceIncidentRepository repo;
    private final UserServiceImpl userService;

    public DeviceIncidentResponse mapToResponse(DeviceIncident entity) {
        DeviceIncidentResponse resp = new DeviceIncidentResponse();
        resp.setId(entity.getId());
        resp.setDescription(entity.getDescription());
        resp.setTimeIncident(entity.getTimeIncident());
        resp.setStatus(entity.getStatus().name());
//        resp.setBookingId(entity.getBookingId());
        resp.setIncidentBy(entity.getIncidentBy().getId());
        return resp;
    }

    @Override
    public DeviceIncident create(DeviceIncidentRequest request) {
        DeviceIncident entity = new DeviceIncident();
        entity.setDescription(request.getDescription());
        entity.setTimeIncident(request.getTimeIncident());
        entity.setStatus(request.getStatus());
        entity.setIncidentBy(userService.getById(request.getIncidentBy()));
//        entity.setManager(request.getManagerId());
//        entity.setBookingId(request.getBookingId());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public DeviceIncident getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_INCIENT_NOT_FOUND));
    }

    @Override
    public List<DeviceIncident> getAll() {
        return repo.findAll();
    }

    @Override
    public DeviceIncident update(Integer id, DeviceIncidentRequest request) {
        DeviceIncident entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setDescription(request.getDescription());
        entity.setTimeIncident(request.getTimeIncident());
        entity.setStatus(request.getStatus());
        entity.setIncidentBy(userService.getById(request.getIncidentBy()));
//        entity.setManager(request.getManagerId());
//        entity.setBookingId(request.getBookingId());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
