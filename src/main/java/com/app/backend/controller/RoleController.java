package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.RoleServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
        DataResponse<RoleResponse> response = DataResponse.<RoleResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<RoleResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<RoleResponse> response = DataResponse.<RoleResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<RoleResponse>>> getAll() {
        DataResponse<List<RoleResponse>> response = DataResponse.<List<RoleResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<RoleResponse>> update(@PathVariable Integer id, @RequestBody RoleRequest request) {
        DataResponse<RoleResponse> response = DataResponse.<RoleResponse>builder()
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
