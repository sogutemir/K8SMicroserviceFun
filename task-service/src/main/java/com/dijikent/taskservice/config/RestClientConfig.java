package com.dijikent.taskservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Bean
    public RestClient userServiceRestClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    log.error("Error calling user service: {} {}, status: {}", 
                            request.getMethod(), request.getURI(), response.getStatusCode());
                })
                .build();
                
        log.info("Created RestClient for user-service with baseUrl: {}", userServiceUrl);
        return restClient;
    }
} 