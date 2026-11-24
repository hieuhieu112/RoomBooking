package com.app.backend.service.intf;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.dtos.internal.DeviceQuantityAvailable;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.DeviceBorrowDetail;

public interface DeviceBorrowDetailService {
    DeviceBorrowDetailResponse mapToResponse(DeviceBorrowDetail entity);
    DeviceBorrowDetail create(DeviceBorrowDetailRequest request);
    DeviceBorrowDetail getById(Long id);
//    List<DeviceBorrowDetail> getAll();
    List<DeviceBorrowDetail> getAllByBooking(Integer bookingId);
    DeviceBorrowDetail update(Long id, DeviceBorrowDetailRequest request);
    void delete(Long id);

    void deleteByBookingId(Integer id);
}
