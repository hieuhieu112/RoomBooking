package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.SystemLogServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.app.backend.utils.ValidRequestUtil;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

@RestController
@RequestMapping("/api/v1/systemlogs")
@AllArgsConstructor
public class SystemLogController {
    private final SystemLogServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<SystemLogResponse>> create(@Valid @RequestBody SystemLogRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<SystemLogResponse> response = DataResponse.<SystemLogResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<SystemLogResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<SystemLogResponse> response = DataResponse.<SystemLogResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<SystemLogResponse>>> getAll() {
        DataResponse<List<SystemLogResponse>> response = DataResponse.<List<SystemLogResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<SystemLogResponse>> update(@PathVariable Integer id, @Valid @RequestBody SystemLogRequest request, BindingResult result) {
        ValidRequestUtil.validateRequest(result);
        DataResponse<SystemLogResponse> response = DataResponse.<SystemLogResponse>builder()
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
