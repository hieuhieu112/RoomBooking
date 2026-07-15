package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceModelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

@RestController
@RequestMapping("/api/v1/devicemodels")
@AllArgsConstructor
public class DeviceModelController {
    private final DeviceModelServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceModelResponse>> create(@Valid @RequestBody DeviceModelRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
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
    public ResponseEntity<DataResponse<DeviceModelResponse>> update(@PathVariable Integer id, @Valid @RequestBody DeviceModelRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
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
