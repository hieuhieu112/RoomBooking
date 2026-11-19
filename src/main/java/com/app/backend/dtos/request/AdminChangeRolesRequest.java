package com.app.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record AdminChangeRolesRequest(@NotEmpty Set<@NotBlank String> roles) {
}
