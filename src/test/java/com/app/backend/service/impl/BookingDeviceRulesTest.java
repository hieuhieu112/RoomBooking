package com.app.backend.service.impl;

import com.app.backend.dtos.internal.DeviceQuantityAvailable;
import com.app.backend.dtos.request.BookingRequest;
import com.app.backend.dtos.request.DeviceBorrowDetailRequest;
import com.app.backend.entity.*;
import com.app.backend.entity.enumm.*;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.redis.NotificationPublisher;
import com.app.backend.repository.BookingRepository;
import com.app.backend.service.intf.DeviceBorrowDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Tests the real service's decisions, NOT PostgreSQL locking or transaction behavior. */
class BookingDeviceRulesTest {
    private final BookingRepository bookings = mock(BookingRepository.class);
    private final UserServiceImpl users = mock(UserServiceImpl.class);
    private final RoomServiceImpl rooms = mock(RoomServiceImpl.class);
    private final DeviceServiceImpl devices = mock(DeviceServiceImpl.class);
    private final DeviceBorrowDetailService details = mock(DeviceBorrowDetailService.class);
    private final BookingServiceImpl service = new BookingServiceImpl(bookings, users, rooms, devices,
            mock(NotificationPublisher.class), mock(ManagerGroupServiceImpl.class), details);
    private Booking existing;
    private Device device;

    @BeforeEach
    void setup() {
        Room room = new Room();
        room.setId(1);
        room.setStatus(RoomStatus.ACTIVE);
        when(rooms.getById(1)).thenReturn(room);
        User owner = new User();
        owner.setId(0);
        owner.setRoles(Set.of());
        when(users.getById(anyInt())).thenReturn(owner);
        device = new Device();
        device.setId(10);
        device.setStatus(DeviceStatus.ACTIVE);
        existing = new Booking();
        existing.setId(42);
        existing.setCreatedBy(0);
        existing.setRoom(room);
        existing.setStatus(BookingStatus.DRAFT);
        DeviceBorrowDetail old = new DeviceBorrowDetail();
        old.setDevice(device);
        old.setBooking(existing);
        old.setQuantity(1);
        existing.setDeviceBorrowDetail(List.of(old));
        when(bookings.findById(42)).thenReturn(Optional.of(existing));
        when(bookings.save(any(Booking.class))).thenAnswer(call -> call.getArgument(0));
        when(devices.getByIdsForUpdate(List.of(10))).thenReturn(List.of(device));
        when(devices.getAllQuantityAvailable(any(), anyList(), any(), nullable(Integer.class)))
                .thenReturn(List.of(new DeviceQuantityAvailable(10, "Projector", "QUANTITY", 1)));
    }

    private BookingRequest request() {
        BookingRequest request = new BookingRequest();
        request.setRoomId(1);
        request.setReason("Device test");
        request.setStatus(BookingStatus.DRAFT);
        request.setStartTime(LocalDateTime.of(2030, 1, 1, 9, 0));
        request.setEndTime(request.getStartTime().plusHours(1));
        DeviceBorrowDetailRequest item = new DeviceBorrowDetailRequest();
        item.setDeviceId(10);
        item.setQuantity(1);
        request.setDeviceBorrowDetail(List.of(item));
        return request;
    }

    @Test
    void createLocksBeforeReadingAvailabilityAndSaving() {
        service.create(request());
        var order = inOrder(devices, bookings);
        order.verify(devices).getByIdsForUpdate(List.of(10));
        order.verify(devices).getAllQuantityAvailable(any(), eq(List.of(10)), any(), isNull());
        order.verify(bookings).save(any(Booking.class));
    }

    @Test
    void updateLocksBeforeReadingAvailabilityAndReplacingDetails() {
        service.update(42, request());
        var order = inOrder(devices, details, bookings);
        order.verify(devices).getByIdsForUpdate(List.of(10));
        order.verify(devices).getAllQuantityAvailable(any(), eq(List.of(10)), any(), eq(42));
        order.verify(details).deleteByBookingId(42);
        order.verify(bookings).save(any(Booking.class));
    }

    @Test
    void missingDeviceIsBusinessErrorBeforeAvailabilityForCreateAndUpdate() {
        when(devices.getByIdsForUpdate(List.of(10))).thenReturn(List.of());
        assertEquals(ErrorCode.DEVICE_NOT_FOUND,
                assertThrows(CommonException.class, () -> service.create(request())).getErrorCode());
        assertEquals(ErrorCode.DEVICE_NOT_FOUND,
                assertThrows(CommonException.class, () -> service.update(42, request())).getErrorCode());
        verify(devices, never()).getAllQuantityAvailable(any(), anyList(), any(), nullable(Integer.class));
    }

    @Test
    void inactiveDeviceIsBusinessErrorBeforeAvailabilityForCreateAndUpdate() {
        device.setStatus(java.util.Arrays.stream(DeviceStatus.values())
                .filter(status -> status != DeviceStatus.ACTIVE).findFirst().orElseThrow());
        assertEquals(ErrorCode.DEVICE_INACTIVE,
                assertThrows(CommonException.class, () -> service.create(request())).getErrorCode());
        assertEquals(ErrorCode.DEVICE_INACTIVE,
                assertThrows(CommonException.class, () -> service.update(42, request())).getErrorCode());
        verify(devices, never()).getAllQuantityAvailable(any(), anyList(), any(), nullable(Integer.class));
    }

    @Test
    void duplicateIdsAreRejectedBeforeLocking() {
        BookingRequest request = request();
        var item = request.getDeviceBorrowDetail().get(0);
        request.setDeviceBorrowDetail(List.of(item, item));
        assertEquals(ErrorCode.DEVICE_BORROW_DUPLICATE_DEVICE,
                assertThrows(CommonException.class, () -> service.create(request)).getErrorCode());
        assertEquals(ErrorCode.DEVICE_BORROW_DUPLICATE_DEVICE,
                assertThrows(CommonException.class, () -> service.update(42, request)).getErrorCode());
        verify(devices, never()).getByIdsForUpdate(anyList());
    }

    @Test
    void nullDeviceListRemovesExistingDevicesAsRequestedByProductOwner() {
        BookingRequest request = request();
        request.setDeviceBorrowDetail(null);
        service.update(42, request);
        // Intentionally a regression test: current implementation must satisfy this contract.
        verify(details).deleteByBookingId(42);
    }
}
