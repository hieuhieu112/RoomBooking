package com.app.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.app.backend.dtos.internal.AuthContext;
import com.app.backend.entity.*;
import com.app.backend.entity.enumm.BookingStatus;
import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.redis.NotificationEvent;
import com.app.backend.redis.NotificationPublisher;
import com.app.backend.service.AuthContextService;
import com.app.backend.service.intf.DeviceBorrowDetailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final DeviceBorrowDetailService deviceBorrowDetailService;

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
        if(user.isAdmin()) return true;
        if(
                user.getManagerGroup() == null
        ){
            return false;
        }
        else{
            ManagerGroup managerGroup = managerGroupService.getById( user.getManagerGroup().getId());
            return managerGroup.getRooms().stream().anyMatch(
                    room -> room.getId().equals(entity.getRoom().getId())
            );
        }
    }

    public Boolean checkPermission(Booking booking){

        User currUser = userService.getById(AuthContextService.getContext().getUserId());

        if (currUser.isAdmin()){
            return true;
        }
        List<Role> roleList = currUser.getRoles().stream().filter(role -> role.getIsManager()).toList();
        if (
                currUser.isManager()
                        && currUser.getManagerGroup().getRooms().stream().anyMatch(room -> room.getId().equals(booking.getRoom().getId()))
        ){
            return true;
        }

        if(Objects.equals(booking.getCreatedBy(), currUser.getId())) return true;

        return false;
    }

    public void isBookingValidStatus(BookingRequest request){
        if(request.getStatus().equals(BookingStatus.DRAFT) || request.getStatus().equals(BookingStatus.INPROCESS)){
            throw new CommonException(ErrorCode.BOOKING_STATUS_INVALID);
        }
    }

    @Override
    @Transactional
    public Booking create(BookingRequest request) {
        Booking entity = new Booking();

        validDataBookingRequest(request);

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
        entity.setStatus(request.getStatus());

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

    @Override
    public Booking update(Integer id, BookingRequest request) {
        Booking entity = getById(id);
        validDataBookingRequest(request);

        if(!checkPermission(entity)) throw  new CommonException(ErrorCode.AUTH_NOT_ENOUGH);

        Room room = roomService.getById(request.getRoomId());
        if(!room.isValidStatus()){
            throw new CommonException(ErrorCode.ROOM_NOT_AVAILABLE);
        }

        if(isRoomBooked(room.getId(), request.getStartTime(),request.getEndTime())){
            throw  new CommonException(ErrorCode.ROOM_ALREADY_BOOKED);
        }

        entity.setRoom(roomService.getById(request.getRoomId()));

        entity.setReason(request.getReason());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setActualEndTime(request.getEndTime().plusMinutes(room.getCleaningDurationMinutes()));


        entity.setStatus(request.getStatus());
//        entity.setUserUsing(userService.getById(request.getUserId()));
//        entity.setUserApproved(userService.getById(request.getApprovedByUserId()));
        entity = repo.save(entity);
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

    // get boooking detail : include device
    public BookingResponse getBookingResponseDetailById(Integer id) {
        User u = userService.getById(AuthContextService.getContext().getUserId());
        Booking booking = getById(id);

        if (!checkPermission(booking)) throw new CommonException(ErrorCode.AUTH_NOT_ENOUGH);

        BookingResponse bookingResponse = mapToResponse(booking);
        List<DeviceBorrowDetail> deviceBorrowDetail = deviceBorrowDetailService.getAllByBooking(id);
        bookingResponse.setDeviceBorrowDetailResponses(
                deviceBorrowDetail.stream().map(deviceBorrowDetailService::mapToResponse).toList()
        );
        return bookingResponse;
    }


    @Override // find by created and filter
    public Page<Booking> getFilterByCurrAccount(
            int size, int page
            ,BookingStatus status,LocalDateTime startTime, LocalDateTime endTime,
            List<Integer> roomIDs
    ) {

        Integer statusNumber = null;
        if (status != null) statusNumber = status.getValue();

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Integer userid = AuthContextService.getContext().getUserId();

        return repo.findAllBookingManager(
                userid,
                -1,
                startTime == null ? null : startTime.toString(),
                statusNumber,
                endTime == null ? null : endTime.toString(),
                roomIDs,
                pageable
        );
    }

    // find all and filter
    public Page<Booking> getFilterByAdmin(int size, int page,
                                          Integer status,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          List<Integer> roomID
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repo.findBookingFilter(
                startTime == null ? null : startTime.toString(),
                status,
                roomID,
                endTime == null ? null : endTime.toString(), pageable
        );
    }

    // get all booking approve, only approve, not include createdBy
    public Page<Booking> getBookingApprove(
            int size, int page,
            BookingStatus status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            List<Integer> roomID
    ) {
        User user = userService.getById(AuthContextService.getContext().getUserId());
        Integer statusNumber = null;

        if (status != null) statusNumber = status.getValue();
        if(
                user.isAdmin()
        ){
            return getFilterByAdmin(size, page,statusNumber,startTime,endTime,roomID);
        };
        if(
                user.isManager()
        ){
            return getBookingApproveManager(size, page,statusNumber,startTime,endTime,roomID);
        };

        throw new CommonException(ErrorCode.AUTH_NOT_ENOUGH);
    }

    //// get all booking approve, only approve by manager
    public Page<Booking> getBookingApproveManager(
            int size, int page,
            Integer status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            List<Integer> roomIDs
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        User user = userService.getById(AuthContextService.getContext().getUserId());
        if(
                user.getManagerGroup() == null || !user.isManager()
        ){
            throw new CommonException(ErrorCode.USER_IS_NOT_MANAGER);
        }


        return repo.findAllBookingManager(
                -1,// not get created
                user.getManagerGroup().getId(),
                startTime == null ? null : startTime.toString(),
                status,
                endTime == null ? null : endTime.toString(),
                roomIDs,
                pageable
        );
    }

    //get all
    public Page<Booking> getAllByFilter(
            int size, int page,
            BookingStatus status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            List<Integer> roomID
    ) {
        Integer statusNumber = null;
        if (status != null) statusNumber = status.getValue();
        if(
                AuthContextService.getContext().isFullAccess()
        ){
            return getFilterByAdmin(size, page,statusNumber,startTime,endTime,roomID);
        };
        User currUser = userService.getById(AuthContextService.getContext().getUserId());
        if (currUser.isManager() && currUser.getManagerGroup() != null)
        {
            return getFilterByManager(size, page,statusNumber,startTime,endTime,roomID);
        }

        return getFilterByCurrAccount(size,page,status,startTime,endTime,roomID);
    }




    public Page<Booking> getFilterByManager(int size, int page,
                                            Integer status,
                                            LocalDateTime startTime,
                                            LocalDateTime endTime,
                                            List<Integer> roomIDs
    ) {
        User user = userService.getById(AuthContextService.getContext().getUserId());
        if(
                user.getManagerGroup() == null
        ){
            throw new CommonException(ErrorCode.USER_IS_NOT_MANAGER);
        }

//        ManagerGroup managerGroup = managerGroupService.getById( user.getManagerGroup().getId());
//        managerGroup.getRooms().stream().map(room -> roomIDs.add(room.getId()));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repo.findAllBookingManager(
                user.getId(),
                user.getManagerGroup().getId(),
                startTime == null ? null : startTime.toString(),
                status,
                endTime == null ? null : endTime.toString(),
                roomIDs,
                pageable
        );
    }



//=======================================


    @Override
    public Booking approval(Integer id) {
        Booking entity = getById(id);

        if(!checkCanApprove(entity)){
            throw new CommonException(ErrorCode.USER_NOT_AUTH_BOOKING);
        }

        roomService.checkRoomStatus(entity.getRoom());


        entity.setStatus(BookingStatus.BOOKED);

        User user = userService.getById(AuthContextService.getContext().getUserId());
        entity.setUserApproved(user);

        repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) {
        Booking entity = getById(id);
        if(!checkPermission(entity)) throw  new CommonException(ErrorCode.AUTH_NOT_ENOUGH);
        repo.deleteById(id);
    }

    @Override
    public Boolean isRoomBooked(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {

        return repo.existsOverlappingBookings(roomId, startTime,endTime);
    }

    private void validDataBookingRequest(BookingRequest b){
        if (b.getStartTime().isAfter(b.getEndTime()) || b.getStartTime().equals(b.getEndTime())){
            throw  new CommonException(ErrorCode.BOOKING_TIME_INVALID);
        }

        isBookingValidStatus(b);
    }
}
