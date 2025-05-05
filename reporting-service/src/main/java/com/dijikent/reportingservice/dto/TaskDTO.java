package com.dijikent.reportingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    
    private Long id;
    private String title;
    private boolean completed;
    private Long userId;
} 