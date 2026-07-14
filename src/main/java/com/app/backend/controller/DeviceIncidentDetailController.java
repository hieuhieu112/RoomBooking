package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceIncidentDetailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.DeviceIncidentDetailService;

@RestController
@RequestMapping("/api/v1/deviceincidentdetails")
@AllArgsConstructor
public class DeviceIncidentDetailController {
    private final DeviceIncidentDetailServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceIncidentDetailResponse>> create(@RequestBody DeviceIncidentDetailRequest request) {
        DataResponse<DeviceIncidentDetailResponse> response = DataResponse.<DeviceIncidentDetailResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceIncidentDetailResponse>> getById(@PathVariable Long id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceIncidentDetailResponse> response = DataResponse.<DeviceIncidentDetailResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceIncidentDetailResponse>>> getAll() {
        DataResponse<List<DeviceIncidentDetailResponse>> response = DataResponse.<List<DeviceIncidentDetailResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceIncidentDetailResponse>> update(@PathVariable Long id, @RequestBody DeviceIncidentDetailRequest request) {
        DataResponse<DeviceIncidentDetailResponse> response = DataResponse.<DeviceIncidentDetailResponse>builder()
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
