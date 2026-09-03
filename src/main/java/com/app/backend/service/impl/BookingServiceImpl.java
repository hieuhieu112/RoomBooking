package com.app.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.app.backend.entity.*;
import com.app.backend.entity.enumm.BookingStatus;
import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.redis.NotificationEvent;
import com.app.backend.redis.NotificationPublisher;
import com.app.backend.service.AuthContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.repository.BookingRepository;
import com.app.backend.service.intf.BookingService;

@Service
@Transactional
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repo;
    private final UserServiceImpl userService;
    private final RoomServiceImpl roomService;
    private final DeviceServiceImpl deviceService;
    private final NotificationPublisher notificationPublisher;
    private final ManagerGroupServiceImpl managerGroupService;

    public BookingResponse mapToResponse(Booking entity) {
        BookingResponse resp = new BookingResponse();
        resp.setId(entity.getId());
        resp.setReason(entity.getReason());
        resp.setStartTime(entity.getStartTime());
        resp.setEndTime(entity.getEndTime());
        resp.setStatus(entity.getStatus().name());
        resp.setUserId(entity.getUserUsing().getId());
        resp.setRoomId(entity.getRoom().getId());
        if(entity.getUserApproved()  != null){
            resp.setApprovedByUserId(entity.getUserApproved().getId());
        }

//        resp.setCanApprove(checkCanApprove(entity));

        return resp;
    }


    public Boolean checkCanApprove(Booking entity){
        User user = userService.getById(AuthContextService.getContext().getUserId());
        if(
                user.getManagerGroup() == null
        ){
            return false;
        }
        else{
            ManagerGroup managerGroup = managerGroupService.getById( user.getManagerGroup().getId());
            if (managerGroup.getRooms().stream().anyMatch(
                    room -> room.getId().equals(entity.getRoom().getId())
            )
            ){
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Booking create(BookingRequest request) {
        Booking entity = new Booking();
        Room room = roomService.getById(request.getRoomId());
        if(!room.isValidStatus()){
            throw new CommonException(ErrorCode.ROOM_NOT_AVAILABLE);
        }

        if(isRoomBooked(room.getId(), request.getStartTime(),request.getEndTime())){
            throw  new CommonException(ErrorCode.ROOM_ALREADY_BOOKED);
        }

        entity.setReason(request.getReason());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setActualEndTime(request.getEndTime().plusMinutes(room.getCleaningDurationMinutes()));
        entity.setStatus(BookingStatus.INPROCESS);
        entity.setUserUsing(userService.getById(AuthContextService.getContext().getUserId()));
        entity.setRoom(room);
        entity.setStatus(BookingStatus.CREATED);

        //devices:

        List<DeviceBorrowDetail> deviceBorrowDetails = new ArrayList<>();
        for(DeviceBorrowDetailRequest devicerq: request.getDeviceBorrowDetail()){
            Device device = deviceService.getById(devicerq.getDeviceId());
            DeviceBorrowDetail deviceBorrowDetail = new DeviceBorrowDetail();
            deviceBorrowDetail.setBooking(entity);
            deviceBorrowDetail.setDevice(device);
            deviceBorrowDetails.add(deviceBorrowDetail);
        }
        entity.setDeviceBorrowDetail(deviceBorrowDetails);

        entity.setUserApproved(null);
        entity = repo.save(entity);

        sendNotificationToApproval(room, room.getManagerGroup().getUsers(), entity.getId());

        return (entity);
    }


    private void sendNotificationToApproval(Room room, List<User> recevier, Integer id){
        for(User u: recevier){
            notificationPublisher.sendMessage(NotificationEvent.builder()
                    .userId(u.getId())
                    .type(NotificationType.CREATE_BOOKING)
                    .title("Phòng "+ room.getName()+ "-" + room.getId() + "có yêu cầu đặt phòng")
                    .username(u.getUsername())
                    .content("Người dùng " + AuthContextService.getContext().getUsername() + " có yêu cầu đặt phòng " + room.getName()+ "-" + room.getId())
                    .referenceId(id.longValue())
                    .build());
        }

    }

    @Override
    public Booking getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public List<Booking> getAllByPermission() {
        if(
                AuthContextService.getContext().isFullAccess()
        ){
            return getAllByAdmin();
        }
        else {
            return getAllByUser();
        }
    }

    @Override
    public List<Booking> getAllByAdmin() {
        return repo.findAll();
    }

    @Override
    public List<Booking> getAllByUser() {
        User user = userService.getById(AuthContextService.getContext().getUserId());
        if(
                user.getManagerGroup() == null
        ){
            throw new CommonException(ErrorCode.USER_IS_NOT_MANAGER);
        }

        ManagerGroup managerGroup = managerGroupService.getById( user.getManagerGroup().getId());

        List<Booking> bookings = repo.findByRoomIds(managerGroup.getRooms().stream().map(room -> room.getId()).toList());
        return bookings;
    }

    @Override
    public Booking approval(Integer id) {
        Booking entity = getById(id);
        if(!checkCanApprove(entity)){
            throw new CommonException(ErrorCode.USER_NOT_AUTH_BOOKING);
        }

        entity.setStatus(BookingStatus.BOOKED);

        User user = userService.getById(AuthContextService.getContext().getUserId());
        entity.setUserApproved(user);

        repo.save(entity);
        return entity;
    }

    @Override
    public Booking update(Integer id, BookingRequest request) {
        Booking entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setReason(request.getReason());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
//        entity.setStatus(request.getStatus());
//        entity.setUserUsing(userService.getById(request.getUserId()));
        entity.setRoom(roomService.getById(request.getRoomId()));
//        entity.setUserApproved(userService.getById(request.getApprovedByUserId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Boolean isRoomBooked(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {

        return repo.existsOverlappingBookings(roomId, startTime,endTime);
    }

    private void validDataBooking(Booking b){
        if (b.getStartTime().isAfter(b.getEndTime())){
            throw  new CommonException(ErrorCode.ROOM_INVALID);
        }
    }
}
