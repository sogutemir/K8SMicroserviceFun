package com.dijikent.userservice.controller;

import com.dijikent.userservice.dto.UserDto;
import com.dijikent.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        UserDto userDto = userService.getUserById(id);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        log.warn("User not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Creating new user: {}", userDto);
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        UserDto updatedUser = userService.updateUser(id, userDto);
        if (updatedUser != null) {
            log.info("User updated successfully");
            return ResponseEntity.ok(updatedUser);
        }
        log.warn("User not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully");
        return ResponseEntity.noContent().build();
    }
} 