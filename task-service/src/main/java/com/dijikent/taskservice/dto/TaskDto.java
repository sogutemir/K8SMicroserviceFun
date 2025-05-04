package com.dijikent.taskservice.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private boolean completed;
    private Long userId;
    
    public TaskDto() {
    }
    
    public TaskDto(Long id, String title, boolean completed, Long userId) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", userId=" + userId +
                '}';
    }
} 