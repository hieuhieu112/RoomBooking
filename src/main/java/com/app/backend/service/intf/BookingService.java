package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Booking;

public interface BookingService {
    BookingResponse mapToResponse(Booking entity);
    Booking create(BookingRequest request);
    Booking getById(Integer id);
    List<Booking> getAll();
    Booking update(Integer id, BookingRequest request);
    void delete(Integer id);
}
