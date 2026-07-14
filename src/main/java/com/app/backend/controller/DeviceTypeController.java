package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.DeviceTypeService;

@RestController
@RequestMapping("/api/v1/devicetypes")
@AllArgsConstructor
public class DeviceTypeController {
    private final DeviceTypeServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceTypeResponse>> create(@RequestBody DeviceTypeRequest request) {
        DataResponse<DeviceTypeResponse> response = DataResponse.<DeviceTypeResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceTypeResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceTypeResponse> response = DataResponse.<DeviceTypeResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceTypeResponse>>> getAll() {
        DataResponse<List<DeviceTypeResponse>> response = DataResponse.<List<DeviceTypeResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceTypeResponse>> update(@PathVariable Integer id, @RequestBody DeviceTypeRequest request) {
        DataResponse<DeviceTypeResponse> response = DataResponse.<DeviceTypeResponse>builder()
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
