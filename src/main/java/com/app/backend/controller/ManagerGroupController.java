package com.app.backend.controller;

import java.util.List;

import com.app.backend.service.impl.ManagerGroupServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.ManagerGroupService;

@RestController
@RequestMapping("/api/v1/managergroups")
@AllArgsConstructor
public class ManagerGroupController {
    private final ManagerGroupServiceImpl service;

    @PostMapping
    public ResponseEntity<DataResponse<ManagerGroupResponse>> create(@RequestBody ManagerGroupRequest request) {
        DataResponse<ManagerGroupResponse> response = DataResponse.<ManagerGroupResponse>builder()
                .data(service.mapToResponse(service.create(request)))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<ManagerGroupResponse>> getById(@PathVariable Integer id) {
        var res = service.getById(id);
        if (res != null) {
            DataResponse<ManagerGroupResponse> response = DataResponse.<ManagerGroupResponse>builder()
                    .data(service.mapToResponse(res))
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<ManagerGroupResponse>>> getAll() {
        DataResponse<List<ManagerGroupResponse>> response = DataResponse.<List<ManagerGroupResponse>>builder()
                .data(service.getAll().stream().map(service::mapToResponse).toList())
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<ManagerGroupResponse>> update(@PathVariable Integer id, @RequestBody ManagerGroupRequest request) {
        DataResponse<ManagerGroupResponse> response = DataResponse.<ManagerGroupResponse>builder()
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
