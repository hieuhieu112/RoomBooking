package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.DeviceService;

@RestController
@RequestMapping("/api/v1/devices")
@AllArgsConstructor
public class DeviceController {
    private final DeviceServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceResponse>> create(@RequestBody DeviceRequest request) {
        DataResponse<DeviceResponse> response = DataResponse.<DeviceResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceResponse> response = DataResponse.<DeviceResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceResponse>>> getAll() {
        DataResponse<List<DeviceResponse>> response = DataResponse.<List<DeviceResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceResponse>> update(@PathVariable Integer id, @RequestBody DeviceRequest request) {
        DataResponse<DeviceResponse> response = DataResponse.<DeviceResponse>builder()
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
