package com.dijikent.reportingservice.client;

import com.dijikent.reportingservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {
    
    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    
    public UserClient(RestTemplate restTemplate, 
                     @Value("${service.user.url:http://localhost:8081}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }
    
    public UserDTO getUserById(Long userId) {
        String url = userServiceUrl + "/api/users/" + userId;
        return restTemplate.getForObject(url, UserDTO.class);
    }
    
    public UserDTO[] getAllUsers() {
        String url = userServiceUrl + "/api/users";
        return restTemplate.getForObject(url, UserDTO[].class);
    }
} 