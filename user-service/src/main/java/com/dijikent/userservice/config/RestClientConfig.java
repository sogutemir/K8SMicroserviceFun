package com.dijikent.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient defaultRestClient() {
        RestClient restClient = RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    log.error("Error making external service call: {} {}, status: {}", 
                            request.getMethod(), request.getURI(), response.getStatusCode());
                })
                .build();
                
        log.info("Created default RestClient for external service calls");
        return restClient;
    }
} 