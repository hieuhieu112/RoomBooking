package com.app.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.entity.Room;
import com.app.backend.service.impl.RoomServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomServiceImpl service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataResponse<RoomResponse>> create(
            @Valid @ModelAttribute RoomRequest request,
            BindingResult result,
            @RequestPart("images") List<MultipartFile> images
    ) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<RoomResponse> response = DataResponse.<RoomResponse>builder()
                .data(service.mapToResponse(service.create(request, images)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<RoomResponse>> getById(@PathVariable Integer id) {

        DataResponse<RoomResponse> response = DataResponse.<RoomResponse>builder()
                .data(service.mapToResponse(service.getById(id)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<DataResponse<Page<RoomResponse>>> getAvailableRoom(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam int page, @RequestParam int size
    ) {

        DataResponse<Page<RoomResponse>> response = DataResponse.<Page<RoomResponse>>builder()
                .data(service.roomResponsePage(service.getRoomAvaiable(start, end, page, size)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DataResponse<Page<RoomResponse>>> getAll(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<Page<RoomResponse>> response = DataResponse.<Page<RoomResponse>>builder()
                .data(service.roomResponsePage(service.getAllPagin(page, size)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/filter")
    public ResponseEntity<DataResponse<Page<RoomResponse>>> getByFilter(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Integer houseId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Room> a = service.getAllByFilter(search, houseId, roomTypeId, page, size);
        DataResponse<Page<RoomResponse>> response = DataResponse.<Page<RoomResponse>>builder()

                .data(a.map(service::mapToResponse))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataResponse<RoomResponse>> update(
            @PathVariable Integer id,
            @Valid @ModelAttribute RoomRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            BindingResult result
    ) {
        ValidRequestUtil.validateRequest(result);
        Room r = service.update(id, request, images);

        DataResponse<RoomResponse> response = DataResponse.<RoomResponse>builder()
                .data(service.mapToResponse(r))
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
