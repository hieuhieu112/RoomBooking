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
import com.app.backend.entity.Booking;
import com.app.backend.repository.BookingRepository;
import com.app.backend.service.BookingService;

@Service
@Transactional
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repo;
    private final UserServiceImpl userService;
    private final RoomServiceImpl roomService;

    public BookingResponse mapToResponse(Booking entity) {
        BookingResponse resp = new BookingResponse();
        resp.setId(entity.getId());
        resp.setReason(entity.getReason());
        resp.setStartTime(entity.getStartTime());
        resp.setEndTime(entity.getEndTime());
        resp.setStatus(entity.getStatus().name());
        resp.setUserId(entity.getUserUsing().getId());
        resp.setRoomId(entity.getRoom().getId());
        resp.setApprovedByUserId(entity.getUserApproved().getId());
        return resp;
    }

    @Override
    public Booking create(BookingRequest request) {
        Booking entity = new Booking();
        entity.setReason(request.getReason());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setStatus(request.getStatus());
        entity.setUserUsing(userService.getById(request.getUserId()));
        entity.setRoom(roomService.getById(request.getRoomId()));
        entity.setUserApproved(userService.getById(request.getApprovedByUserId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public Booking getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public List<Booking> getAll() {
        return repo.findAll();
    }

    @Override
    public Booking update(Integer id, BookingRequest request) {
        Booking entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setReason(request.getReason());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setStatus(request.getStatus());
        entity.setUserUsing(userService.getById(request.getUserId()));
        entity.setRoom(roomService.getById(request.getRoomId()));
        entity.setUserApproved(userService.getById(request.getApprovedByUserId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
