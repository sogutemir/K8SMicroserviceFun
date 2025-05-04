package com.dijikent.taskservice.service;

import com.dijikent.taskservice.client.UserServiceClient;
import com.dijikent.taskservice.dto.TaskDto;
import com.dijikent.taskservice.dto.TaskWithUserDto;
import com.dijikent.taskservice.dto.UserDto;
import com.dijikent.taskservice.model.Task;
import com.dijikent.taskservice.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserServiceClient userServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }
    
    public TaskWithUserDto getTaskWithUserById(Long id) {
        log.info("Getting task with user details for task ID: {}", id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            UserDto userDto = userServiceClient.getUserById(task.getUserId());
            
            if (userDto != null) {
                log.info("Successfully retrieved user details for task ID: {}", id);
                return new TaskWithUserDto(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted(),
                        userDto
                );
            } else {
                log.warn("User details not found for task ID: {} with user ID: {}", id, task.getUserId());
                return new TaskWithUserDto(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted(),
                        null
                );
            }
        }
        
        log.warn("Task not found with ID: {}", id);
        return null;
    }

    public List<TaskDto> getTasksByUserId(Long userId) {
        if (!userServiceClient.userExists(userId)) {
            log.warn("User with ID {} does not exist", userId);
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }
        
        log.info("Fetching tasks for user ID: {}", userId);
        return taskRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto createTask(TaskDto taskDto) {
        if (!userServiceClient.userExists(taskDto.getUserId())) {
            log.warn("Cannot create task. User with ID {} does not exist", taskDto.getUserId());
            throw new IllegalArgumentException("User with ID " + taskDto.getUserId() + " does not exist");
        }
        
        log.info("Creating new task for user ID: {}", taskDto.getUserId());
        Task task = convertToEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        log.info("Task created with ID: {}", savedTask.getId());
        return convertToDto(savedTask);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        if (taskRepository.existsById(id)) {
            if (!userServiceClient.userExists(taskDto.getUserId())) {
                log.warn("Cannot update task. User with ID {} does not exist", taskDto.getUserId());
                throw new IllegalArgumentException("User with ID " + taskDto.getUserId() + " does not exist");
            }
            
            log.info("Updating task with ID: {} for user ID: {}", id, taskDto.getUserId());
            Task task = convertToEntity(taskDto);
            task.setId(id);
            Task updatedTask = taskRepository.save(task);
            log.info("Task updated successfully");
            return convertToDto(updatedTask);
        }
        log.warn("Cannot update task. Task with ID {} not found", id);
        return null;
    }

    public void deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        log.info("Task deleted successfully");
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.isCompleted(), task.getUserId());
    }

    private Task convertToEntity(TaskDto taskDto) {
        return new Task(taskDto.getId(), taskDto.getTitle(), taskDto.isCompleted(), taskDto.getUserId());
    }
} 