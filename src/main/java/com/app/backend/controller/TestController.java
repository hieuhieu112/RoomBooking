package com.app.backend.controller;

import com.app.backend.service.impl.RoomServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@AllArgsConstructor
public class TestController {
    private final RoomServiceImpl service;
}
