package com.app.backend.controller;

import com.app.backend.dtos.request.AdminChangeRolesRequest;
import com.app.backend.dtos.request.AdminLockUserRequest;
import com.app.backend.dtos.request.AdminResetPasswordRequest;
import com.app.backend.dtos.response.DataResponse;
import com.app.backend.dtos.response.StatusRes;
import com.app.backend.service.impl.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService service;

    @PatchMapping("/{id}/password")
    public ResponseEntity<DataResponse<Void>> resetPassword(
            @PathVariable Integer id, @Valid @RequestBody AdminResetPasswordRequest request) {
        service.resetPassword(id, request.newPass());
        return success();
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<DataResponse<Void>> changeRoles(
            @PathVariable Integer id, @Valid @RequestBody AdminChangeRolesRequest request) {
        service.changeRoles(id, request.roles());
        return success();
    }

    @PatchMapping("/{id}/lock")
    public ResponseEntity<DataResponse<Void>> setLocked(
            @PathVariable Integer id, @Valid @RequestBody AdminLockUserRequest request) {
        service.setLocked(id, request.locked());
        return success();
    }

    private ResponseEntity<DataResponse<Void>> success() {
        return ResponseEntity.ok(DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS).message("SUCCESS").build());
    }
}
