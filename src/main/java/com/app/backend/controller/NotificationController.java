package com.app.backend.controller;

import com.app.backend.dtos.response.DataResponse;
import com.app.backend.dtos.response.NotificationResponse;
import com.app.backend.dtos.response.RoleResponse;
import com.app.backend.dtos.response.StatusRes;
import com.app.backend.entity.Notification;
import com.app.backend.entity.enumm.NotificationType;
import com.app.backend.service.impl.NotificationServiceIml;
import com.app.backend.service.intf.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@AllArgsConstructor
public class NotificationController {
    private final NotificationServiceIml notificationServiceIml;

    @GetMapping()
    public ResponseEntity<DataResponse<List<NotificationResponse>>> getAll() {
        var res = notificationServiceIml.getByUser();
        if (res != null) {
            DataResponse<List<NotificationResponse>> response = DataResponse.<List<NotificationResponse>>builder()
                    .data(res.stream().map(notificationServiceIml::mapToResponse).toList())
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{id}/read")
    public ResponseEntity<DataResponse<Void>> readNotification(@PathVariable Long id) {
        Notification n =  notificationServiceIml.markAsRead(id);

        DataResponse<Void> response = DataResponse.<Void>builder()
                .data(null)
                .path(notificationServiceIml.getPath(n))
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        ResponseEntity<DataResponse<Void>>  a = ResponseEntity.ok(response);
        return a;
    }

    @GetMapping(value = "/count-unread")
    public ResponseEntity<DataResponse<Long>> getUnread() {
        Long res = notificationServiceIml.countUnread();

        DataResponse<Long> response = DataResponse.<Long>builder()
                .data(res)
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);

    }
}