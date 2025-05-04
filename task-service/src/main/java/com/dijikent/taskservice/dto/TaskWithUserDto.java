package com.dijikent.taskservice.dto;

import lombok.Data;

@Data
public class TaskWithUserDto {
    private Long id;
    private String title;
    private boolean completed;
    private UserDto user;
    
    public TaskWithUserDto() {
    }
    
    public TaskWithUserDto(Long id, String title, boolean completed, UserDto user) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.user = user;
    }

    @Override
    public String toString() {
        return "TaskWithUserDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", user=" + user +
                '}';
    }
} 