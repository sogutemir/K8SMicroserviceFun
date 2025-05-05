package com.dijikent.reportingservice.client;

import com.dijikent.reportingservice.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TaskClient {
    
    private final RestTemplate restTemplate;
    private final String taskServiceUrl;
    
    public TaskClient(RestTemplate restTemplate, 
                     @Value("${service.task.url:http://localhost:8082}") String taskServiceUrl) {
        this.restTemplate = restTemplate;
        this.taskServiceUrl = taskServiceUrl;
    }
    
    public TaskDTO getTaskById(Long taskId) {
        String url = taskServiceUrl + "/api/tasks/" + taskId;
        return restTemplate.getForObject(url, TaskDTO.class);
    }
    
    public TaskDTO[] getAllTasksByUserId(Long userId) {
        String url = taskServiceUrl + "/api/tasks/user/" + userId;
        return restTemplate.getForObject(url, TaskDTO[].class);
    }
    
    public TaskDTO[] getAllTasks() {
        String url = taskServiceUrl + "/api/tasks";
        return restTemplate.getForObject(url, TaskDTO[].class);
    }
} 