package com.dijikent.taskservice.controller;

import com.dijikent.taskservice.dto.TaskDto;
import com.dijikent.taskservice.dto.TaskWithUserDto;
import com.dijikent.taskservice.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/tasks")
public class TaskController {


    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        log.info("Fetching all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        log.info("Fetching task with ID: {}", id);
        TaskDto taskDto = taskService.getTaskById(id);
        if (taskDto != null) {
            return ResponseEntity.ok(taskDto);
        }
        log.warn("Task not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/with-user")
    public ResponseEntity<TaskWithUserDto> getTaskWithUserById(@PathVariable Long id) {
        log.info("Fetching task with user details for ID: {}", id);
        TaskWithUserDto taskWithUserDto = taskService.getTaskWithUserById(id);
        if (taskWithUserDto != null) {
            return ResponseEntity.ok(taskWithUserDto);
        }
        log.warn("Task not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(@PathVariable Long userId) {
        log.info("Fetching tasks for user ID: {}", userId);
        try {
            return ResponseEntity.ok(taskService.getTasksByUserId(userId));
        } catch (IllegalArgumentException e) {
            log.error("Error fetching tasks for user ID: {}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        log.info("Creating new task: {}", taskDto);
        try {
            return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error creating task", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        log.info("Updating task with ID: {}", id);
        try {
            TaskDto updatedTask = taskService.updateTask(id, taskDto);
            if (updatedTask != null) {
                return ResponseEntity.ok(updatedTask);
            }
            log.warn("Task not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Error updating task with ID: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
} 