package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceModelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.DeviceModelService;

@RestController
@RequestMapping("/api/v1/devicemodels")
@AllArgsConstructor
public class DeviceModelController {
    private final DeviceModelServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceModelResponse>> create(@RequestBody DeviceModelRequest request) {
        DataResponse<DeviceModelResponse> response = DataResponse.<DeviceModelResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceModelResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceModelResponse> response = DataResponse.<DeviceModelResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceModelResponse>>> getAll() {
        DataResponse<List<DeviceModelResponse>> response = DataResponse.<List<DeviceModelResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceModelResponse>> update(@PathVariable Integer id, @RequestBody DeviceModelRequest request) {
        DataResponse<DeviceModelResponse> response = DataResponse.<DeviceModelResponse>builder()
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
