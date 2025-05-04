package com.dijikent.taskservice.client;

import com.dijikent.taskservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class UserServiceClient {

    private final RestClient restClient;

    @Autowired
    public UserServiceClient(RestClient userServiceRestClient) {
        this.restClient = userServiceRestClient;
        log.info("UserServiceClient initialized with RestClient");
    }

    public boolean userExists(Long userId) {
        try {
            String url = "/api/users/" + userId;
            log.info("Checking if user exists at URL: {}", url);
            
            ResponseEntity<UserDto> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(UserDto.class);
                    
            log.info("User exists check response: {}", response.getStatusCode());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error checking if user exists: ", e);
            return false;
        }
    }
    
    public UserDto getUserById(Long userId) {
        try {
            String url = "/api/users/" + userId;
            log.info("Getting user details from URL: {}", url);
            
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(UserDto.class);
                    
        } catch (Exception e) {
            log.error("Error getting user details: ", e);
            return null;
        }
    }
} 