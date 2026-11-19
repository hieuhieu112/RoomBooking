package com.app.backend.service.intf;

import java.time.LocalDateTime;
import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Booking;
import com.app.backend.entity.Room;
import com.app.backend.entity.enumm.BookingStatus;
import org.springframework.data.domain.Page;

public interface BookingService {
    BookingResponse mapToResponse(Booking entity);
    Booking create(BookingRequest request);
    Booking getById(Integer id);
//    Page<Booking> getAllBookingManagementBy(int size, int page);
//    Page<Booking> getAllByAdmin(int size, int page);
//    Page<Booking> getAllByManager(int size, int page);
    Page<Booking> getFilterByCurrAccount(int size, int page, BookingStatus statusNumber, LocalDateTime startTime, LocalDateTime endTime, List<Integer> roomIDs);
    Booking approval(Integer id);
    Booking update(Integer id, BookingRequest request);
    void delete(Integer id);
    Boolean isRoomBooked(Integer roomId, LocalDateTime startTime, LocalDateTime endTime);
}
