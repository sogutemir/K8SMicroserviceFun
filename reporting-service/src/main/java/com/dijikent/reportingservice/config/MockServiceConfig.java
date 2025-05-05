package com.dijikent.reportingservice.config;

import com.dijikent.reportingservice.dto.TaskDTO;
import com.dijikent.reportingservice.dto.UserDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Configuration
public class MockServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "use.mock.service", havingValue = "true")
    public MockRestServiceServer mockRestServiceServer(RestTemplate restTemplate) {
        return MockRestServiceServer.createServer(restTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "use.mock.service", havingValue = "true")
    public String setupMockResponses(
            MockRestServiceServer mockServer,
            RestTemplate restTemplate,
            MockDataConfig mockDataConfig
    ) throws Exception {
        Map<Long, UserDTO> mockUsers = mockDataConfig.mockUserData();
        Map<Long, TaskDTO> mockTasks = mockDataConfig.mockTaskData();
        Map<Long, List<TaskDTO>> mockTasksByUserId = mockDataConfig.mockTasksByUserIdData();

        // Mock user service endpoints
        for (Map.Entry<Long, UserDTO> entry : mockUsers.entrySet()) {
            Long userId = entry.getKey();
            UserDTO user = entry.getValue();
            
            mockServer.expect(ExpectedCount.manyTimes(),
                    requestTo(new URI("http://localhost:8081/api/users/" + userId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"id\":" + user.getId() + 
                                 ",\"name\":\"" + user.getName() + 
                                 "\",\"email\":\"" + user.getEmail() + "\"}"));
        }

        // Mock get all users
        StringBuilder allUsersJson = new StringBuilder("[");
        int userCount = 0;
        for (UserDTO user : mockUsers.values()) {
            allUsersJson.append("{\"id\":").append(user.getId())
                    .append(",\"name\":\"").append(user.getName())
                    .append("\",\"email\":\"").append(user.getEmail()).append("\"}");
            
            userCount++;
            if (userCount < mockUsers.size()) {
                allUsersJson.append(",");
            }
        }
        allUsersJson.append("]");

        mockServer.expect(ExpectedCount.manyTimes(), 
                requestTo(new URI("http://localhost:8081/api/users")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(allUsersJson.toString()));

        // Mock task service endpoints
        for (Map.Entry<Long, TaskDTO> entry : mockTasks.entrySet()) {
            Long taskId = entry.getKey();
            TaskDTO task = entry.getValue();
            
            mockServer.expect(ExpectedCount.manyTimes(),
                    requestTo(new URI("http://localhost:8082/api/tasks/" + taskId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"id\":" + task.getId() + 
                                 ",\"title\":\"" + task.getTitle() + 
                                 "\",\"completed\":" + task.isCompleted() + 
                                 ",\"userId\":" + task.getUserId() + "}"));
        }

        // Mock tasks by user ID
        for (Map.Entry<Long, List<TaskDTO>> entry : mockTasksByUserId.entrySet()) {
            Long userId = entry.getKey();
            List<TaskDTO> tasks = entry.getValue();
            
            StringBuilder tasksJson = new StringBuilder("[");
            for (int i = 0; i < tasks.size(); i++) {
                TaskDTO task = tasks.get(i);
                tasksJson.append("{\"id\":").append(task.getId())
                        .append(",\"title\":\"").append(task.getTitle())
                        .append("\",\"completed\":").append(task.isCompleted())
                        .append(",\"userId\":").append(task.getUserId()).append("}");
                
                if (i < tasks.size() - 1) {
                    tasksJson.append(",");
                }
            }
            tasksJson.append("]");
            
            mockServer.expect(ExpectedCount.manyTimes(),
                    requestTo(new URI("http://localhost:8082/api/tasks/user/" + userId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(tasksJson.toString()));
        }

        // Mock get all tasks
        StringBuilder allTasksJson = new StringBuilder("[");
        int taskCount = 0;
        for (TaskDTO task : mockTasks.values()) {
            allTasksJson.append("{\"id\":").append(task.getId())
                    .append(",\"title\":\"").append(task.getTitle())
                    .append("\",\"completed\":").append(task.isCompleted())
                    .append(",\"userId\":").append(task.getUserId()).append("}");
            
            taskCount++;
            if (taskCount < mockTasks.size()) {
                allTasksJson.append(",");
            }
        }
        allTasksJson.append("]");

        mockServer.expect(ExpectedCount.manyTimes(), 
                requestTo(new URI("http://localhost:8082/api/tasks")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(allTasksJson.toString()));
                        
        return "Mock services initialized";
    }
} 