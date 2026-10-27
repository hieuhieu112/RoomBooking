package com.app.backend.entity.enumm;

public enum BookingStatus {
    CREATED(0),
    PENDING(1),
    APPROVED(2),
    REJECTED(3),
    CANCELED(4),
    BOOKED(5),
    COMPLETED(6),
    INPROCESS(7);

    private final Integer value;

    BookingStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
