package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotNull;

public record AdminLockUserRequest(@NotNull Boolean locked) {
}
