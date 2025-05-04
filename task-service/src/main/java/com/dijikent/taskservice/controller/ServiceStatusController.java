package com.dijikent.taskservice.controller;

import com.dijikent.taskservice.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/status")
public class ServiceStatusController {

    private final UserServiceClient userServiceClient;

    @Autowired
    public ServiceStatusController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getServiceStatus() {
        log.info("Checking service status");
        Map<String, Object> status = new HashMap<>();
        
        // Task service durumu
        status.put("taskService", "UP");
        
        // User service durumu kontrol
        boolean userServiceUp = isUserServiceUp();
        status.put("userService", userServiceUp ? "UP" : "DOWN");
        
        // Detaylar
        status.put("timestamp", System.currentTimeMillis());
        status.put("serviceName", "task-service");
        
        log.info("Service status: {}", status);
        return ResponseEntity.ok(status);
    }
    
    private boolean isUserServiceUp() {
        try {
            // Test amaçlı kullanıcı ID 1 ile deniyoruz
            // Gerçek ortamda farklı bir health check endpointi kullanılabilir
            userServiceClient.getUserById(1L);
            return true;
        } catch (Exception e) {
            log.warn("User service appears to be down: {}", e.getMessage());
            return false;
        }
    }
} 