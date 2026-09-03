package com.app.backend.entity.enumm;

import com.app.backend.entity.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum NotificationType {
    CREATE_BOOKING("/bookings/{id}"),
    UPDATE_BOOKING("/bookings/{id}/edit");

    private final String url;

    NotificationType(String url) {
        this.url = url;
    }

}
