package com.dijikent.userservice.service;

import com.dijikent.userservice.dto.UserDto;
import com.dijikent.userservice.model.User;
import com.dijikent.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        log.info("Getting all users");
        List<UserDto> users = userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Found {} users", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        log.info("Getting user by ID: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    log.info("User found: {}", user);
                    return convertToDto(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found with ID: {}", id);
                    return null;
                });
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Creating new user: {}", userDto);
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return convertToDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        if (userRepository.existsById(id)) {
            User user = convertToEntity(userDto);
            user.setId(id);
            User updatedUser = userRepository.save(user);
            log.info("User updated successfully: {}", updatedUser);
            return convertToDto(updatedUser);
        }
        log.warn("Cannot update user. User with ID {} not found", id);
        return null;
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        log.info("User deleted successfully");
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    private User convertToEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
} 