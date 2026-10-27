package com.app.backend.service.intf;

import java.time.LocalDateTime;
import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Booking;
import com.app.backend.entity.Room;
import org.springframework.data.domain.Page;

public interface BookingService {
    BookingResponse mapToResponse(Booking entity);
    Booking create(BookingRequest request);
    Booking getById(Integer id);
    Page<Booking> getAllByPermission(int size, int page);
    Page<Booking> getAllByAdmin(int size, int page);
    Page<Booking> getAllByUser(int size, int page);
    Page<Booking> getAllMyBooking(int size, int page);
    Booking approval(Integer id);
    Booking update(Integer id, BookingRequest request);
    void delete(Integer id);
    Boolean isRoomBooked(Integer roomId, LocalDateTime startTime, LocalDateTime endTime);
}
