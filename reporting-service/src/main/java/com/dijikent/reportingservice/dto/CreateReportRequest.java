package com.dijikent.reportingservice.dto;

import com.dijikent.reportingservice.model.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest {
    
    private String name;
    private String description;
    private Long userId;
    private ReportType type;
} 