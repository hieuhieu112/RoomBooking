package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceBorrowDetailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.DeviceBorrowDetailService;

@RestController
@RequestMapping("/api/v1/deviceborrowdetails")
@AllArgsConstructor
public class DeviceBorrowDetailController {
    private final DeviceBorrowDetailServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceBorrowDetailResponse>> create(@RequestBody DeviceBorrowDetailRequest request) {
        DataResponse<DeviceBorrowDetailResponse> response = DataResponse.<DeviceBorrowDetailResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceBorrowDetailResponse>> getById(@PathVariable Long id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceBorrowDetailResponse> response = DataResponse.<DeviceBorrowDetailResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceBorrowDetailResponse>>> getAll() {
        DataResponse<List<DeviceBorrowDetailResponse>> response = DataResponse.<List<DeviceBorrowDetailResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceBorrowDetailResponse>> update(@PathVariable Long id, @RequestBody DeviceBorrowDetailRequest request) {
        DataResponse<DeviceBorrowDetailResponse> response = DataResponse.<DeviceBorrowDetailResponse>builder()
                .data(service.mapToResponse(service.update(id, request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }
}
