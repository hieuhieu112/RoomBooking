package com.app.backend.constant;


import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

public final class UserPathConstant {
    private UserPathConstant() {}

    public static final Map<HttpMethod, List<String>> USER_ENDPOINTS = Map.of(
            HttpMethod.GET, List.of(
                    "/rooms",
                    "/rooms/{id:[0-9]+}",
                    "/rooms/filter",
                    "/rooms/available",

                    "/houses",
                    "/houses/{id:[0-9]+}",
                    "/houses/filter",

                    "/roomtypes",
                    "/roomtypes/{id:[0-9]+}",
                    "/roomtypes/filter",

                    "/devices",
                    "/devices/{id:[0-9]+}",
                    "/devices/available",

                    "/devicecategories",
                    "/devicecategories/{id:[0-9]+}",

                    "/devicetypes",
                    "/devicetypes/{id:[0-9]+}",

                    "/devicemodels",
                    "/devicemodels/{id:[0-9]+}",

                    "/manufacturerdevices",
                    "/manufacturerdevices/{id:[0-9]+}",

                    "/bookings/filter",
                    "/bookings/my-booking",
                    "/bookings/{id:[0-9]+}",

                    "/notification",
                    "/notification/count-unread"
            ),

            HttpMethod.POST, List.of(
                    "/bookings",
                    "/notification/{id:[0-9]+}/read",
                    "/authen/logout"
            ),
            HttpMethod.PATCH, List.of(
                    "/bookings/{id:[0-9]+}",
                    "/authen/change_pass"
            ),

            HttpMethod.PUT, List.of(
                    "/bookings/{id:[0-9]+}"
            ),

            HttpMethod.DELETE, List.of(
                    "/bookings/{id:[0-9]+}"
            )
    );
}
