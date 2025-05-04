package com.dijikent.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/status")
public class ServiceStatusController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getServiceStatus() {
        log.info("Checking service status");
        Map<String, Object> status = new HashMap<>();
        
        status.put("userService", "UP");
        
        status.put("timestamp", System.currentTimeMillis());
        status.put("serviceName", "user-service");
        
        log.info("Service status: {}", status);
        return ResponseEntity.ok(status);
    }
} 