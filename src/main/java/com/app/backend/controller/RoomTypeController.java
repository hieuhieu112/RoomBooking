package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.RoomTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

@RestController
@RequestMapping("/api/v1/roomtypes")
@AllArgsConstructor
public class RoomTypeController {
    private final RoomTypeServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<RoomTypeResponse>> create(@Valid @RequestBody RoomTypeRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<RoomTypeResponse> response = DataResponse.<RoomTypeResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<RoomTypeResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<RoomTypeResponse> response = DataResponse.<RoomTypeResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<RoomTypeResponse>>> getAll() {
        DataResponse<List<RoomTypeResponse>> response = DataResponse.<List<RoomTypeResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<RoomTypeResponse>> update(@PathVariable Integer id, @Valid @RequestBody RoomTypeRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<RoomTypeResponse> response = DataResponse.<RoomTypeResponse>builder()
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
