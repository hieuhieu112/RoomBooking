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
import com.app.backend.entity.DeviceBorrowDetail;
import com.app.backend.repository.DeviceBorrowDetailRepository;
import com.app.backend.service.DeviceBorrowDetailService;

@Service
@Transactional
@AllArgsConstructor
public class DeviceBorrowDetailServiceImpl implements DeviceBorrowDetailService {
    private final DeviceBorrowDetailRepository repo;
    private final BookingServiceImpl bookingService;
    private final DeviceServiceImpl deviceService;


    public DeviceBorrowDetailResponse mapToResponse(DeviceBorrowDetail entity) {
        DeviceBorrowDetailResponse resp = new DeviceBorrowDetailResponse();
//        resp.setId(entity.getId());
        resp.setBookingId(entity.getBooking().getId());
        resp.setDeviceId(entity.getDevice().getId());
        resp.setNote(entity.getNote());
        return resp;
    }

    @Override
    public DeviceBorrowDetail create(DeviceBorrowDetailRequest request) {
        DeviceBorrowDetail entity = new DeviceBorrowDetail();
        entity.setBooking(bookingService.getById(request.getBookingId()));
        entity.setDevice(deviceService.getById(request.getDeviceId()));
        entity.setNote(request.getNote());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public DeviceBorrowDetail getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEVICE_NOT_FOUND));
    }

    @Override
    public List<DeviceBorrowDetail> getAll() {
        return repo.findAll();
    }

    @Override
    public DeviceBorrowDetail update(Long id, DeviceBorrowDetailRequest request) {
        DeviceBorrowDetail entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setBooking(bookingService.getById(request.getBookingId()));
        entity.setDevice(deviceService.getById(request.getDeviceId()));
        entity.setNote(request.getNote());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }
}
