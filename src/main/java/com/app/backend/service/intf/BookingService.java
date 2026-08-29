package com.app.backend.service.intf;

import java.time.LocalDateTime;
import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Booking;
import com.app.backend.entity.Room;

public interface BookingService {
    BookingResponse mapToResponse(Booking entity);
    Booking create(BookingRequest request);
    Booking getById(Integer id);
    List<Booking> getAllByPermission();
    List<Booking> getAllByAdmin();
    List<Booking> getAllByUser();
    Booking approval(Integer id);
    Booking update(Integer id, BookingRequest request);
    void delete(Integer id);
    Boolean isRoomBooked(Integer roomId, LocalDateTime startTime, LocalDateTime endTime);
}
