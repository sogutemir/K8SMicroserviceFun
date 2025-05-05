package com.dijikent.reportingservice.dto;

import com.dijikent.reportingservice.model.ReportStatus;
import com.dijikent.reportingservice.model.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private ReportType type;
    private ReportStatus status;
    private Integer taskCount;
    private Integer completedTaskCount;
    private Double completionRate;
} 