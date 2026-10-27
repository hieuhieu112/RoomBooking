package com.app.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.entity.enumm.BookingStatus;
import com.app.backend.service.impl.BookingServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<BookingResponse> response = DataResponse.<BookingResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<BookingResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        BookingResponse a = service.mapToResponse(res);
        a.setCanApprove(service.checkCanApprove(res));
        if (res != null) {
            DataResponse<BookingResponse> response = DataResponse.<BookingResponse>builder()
                    .data(a)
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<Page<BookingResponse>>> getAllByPermission(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<Page<BookingResponse>> response = DataResponse.<Page<BookingResponse>>builder()
                .data(service.getAllByPermission(size, page).map(service::mapToResponse))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/filter")
    public ResponseEntity<DataResponse<Page<BookingResponse>>> filterBooking(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false) List<Integer> roomIds
            ) {
        DataResponse<Page<BookingResponse>> response = DataResponse.<Page<BookingResponse>>builder()
                .data(service.getAllByFilter(
                                size, page,
                                status,startTime,
                                endTime, roomIds
                        )
                        .map(service::mapToResponse))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/my-booking")
    public ResponseEntity<DataResponse<Page<BookingResponse>>> getAllMyBooking(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<Page<BookingResponse>> response = DataResponse.<Page<BookingResponse>>builder()
                .data(service.getAllMyBooking(size, page).map(service::mapToResponse))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/approval")
    public ResponseEntity<DataResponse<List<BookingResponse>>> getAllApproval(
            @RequestParam int size,@RequestParam int page) {
        DataResponse<List<BookingResponse>> response = DataResponse.<List<BookingResponse>>builder()
                .data(service.getAllByPermission(size, page).stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approval")
    public ResponseEntity<DataResponse<BookingResponse>> approval(
            @PathVariable Integer id
    ) {
        DataResponse<BookingResponse> response = DataResponse.<BookingResponse>builder()
                .data(service.mapToResponse(service.approval(id)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<BookingResponse>> update(@PathVariable Integer id, @Valid @RequestBody BookingRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<BookingResponse> response = DataResponse.<BookingResponse>builder()
                .data(service.mapToResponse(service.update(id, request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }
}
