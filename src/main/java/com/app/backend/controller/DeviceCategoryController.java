package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.DeviceCategoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

@RestController
@RequestMapping("/api/v1/devicecategorys")
@AllArgsConstructor
public class DeviceCategoryController {
    private final DeviceCategoryServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<DeviceCategoryResponse>> create(@Valid @RequestBody DeviceCategoryRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<DeviceCategoryResponse> response = DataResponse.<DeviceCategoryResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceCategoryResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<DeviceCategoryResponse> response = DataResponse.<DeviceCategoryResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<DeviceCategoryResponse>>> getAll() {
        DataResponse<List<DeviceCategoryResponse>> response = DataResponse.<List<DeviceCategoryResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<DeviceCategoryResponse>> update(@PathVariable Integer id, @Valid @RequestBody DeviceCategoryRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<DeviceCategoryResponse> response = DataResponse.<DeviceCategoryResponse>builder()
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
