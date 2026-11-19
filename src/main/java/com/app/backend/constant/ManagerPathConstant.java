package com.app.backend.constant;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

public final class ManagerPathConstant {

    private ManagerPathConstant() {}

    public static final Map<HttpMethod, List<String>> MANAGER_ENDPOINTS = Map.of(
            HttpMethod.GET, List.of(
                    "/bookings/approval"
            ),

            HttpMethod.PUT, List.of(
                    "/bookings/{id:[0-9]+}/approval"
            )
    );
}