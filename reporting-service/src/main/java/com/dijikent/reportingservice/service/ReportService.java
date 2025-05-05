package com.dijikent.reportingservice.service;

import com.dijikent.reportingservice.client.TaskClient;
import com.dijikent.reportingservice.client.UserClient;
import com.dijikent.reportingservice.dto.CreateReportRequest;
import com.dijikent.reportingservice.dto.ReportDTO;
import com.dijikent.reportingservice.dto.TaskDTO;
import com.dijikent.reportingservice.dto.UserDTO;
import com.dijikent.reportingservice.model.Report;
import com.dijikent.reportingservice.model.ReportStatus;
import com.dijikent.reportingservice.model.ReportType;
import com.dijikent.reportingservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final UserClient userClient;
    private final TaskClient taskClient;
    
    public ReportDTO createReport(CreateReportRequest request) {
        UserDTO user = userClient.getUserById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }
        
        Report report = new Report();
        report.setName(request.getName());
        report.setDescription(request.getDescription());
        report.setUserId(request.getUserId());
        report.setType(request.getType());
        report.setStatus(ReportStatus.PENDING);
        report.setCreatedAt(LocalDateTime.now());
        report.setTaskCount(0);
        report.setCompletedTaskCount(0);
        
        Report savedReport = reportRepository.save(report);
        
        processReport(savedReport);
        
        return mapToDTO(savedReport);
    }
    
    private void processReport(Report report) {
        try {
            report.setStatus(ReportStatus.PROCESSING);
            reportRepository.save(report);
            
            if (report.getType() == ReportType.USER_PRODUCTIVITY ||
                report.getType() == ReportType.TASK_COMPLETION) {
                processTaskReport(report);
            } else {
                processSummaryReport(report);
            }
            
            report.setStatus(ReportStatus.COMPLETED);
            reportRepository.save(report);
        } catch (Exception e) {
            report.setStatus(ReportStatus.FAILED);
            reportRepository.save(report);
            throw new RuntimeException("Report processing error", e);
        }
    }
    
    private void processTaskReport(Report report) {

        TaskDTO[] tasks = taskClient.getAllTasksByUserId(report.getUserId());
        
        if (tasks == null || tasks.length == 0) {
            report.setTaskCount(0);
            report.setCompletedTaskCount(0);
            return;
        }
        
        int totalTasks = tasks.length;
        int completedTasks = (int) Arrays.stream(tasks)
                .filter(TaskDTO::isCompleted)
                .count();
        
        report.setTaskCount(totalTasks);
        report.setCompletedTaskCount(completedTasks);
    }
    
    private void processSummaryReport(Report report) {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate;
        
        switch (report.getType()) {
            case DAILY_SUMMARY:
                startDate = endDate.minusDays(1);
                break;
            case WEEKLY_SUMMARY:
                startDate = endDate.minusWeeks(1);
                break;
            case MONTHLY_SUMMARY:
                startDate = endDate.minusMonths(1);
                break;
            default:
                startDate = endDate.minusDays(1);
        }
        
        TaskDTO[] allTasks = taskClient.getAllTasks();
        
        if (allTasks == null || allTasks.length == 0) {
            report.setTaskCount(0);
            report.setCompletedTaskCount(0);
            return;
        }
        
        int totalTasks = allTasks.length;
        int completedTasks = (int) Arrays.stream(allTasks)
                .filter(TaskDTO::isCompleted)
                .count();
        
        report.setTaskCount(totalTasks);
        report.setCompletedTaskCount(completedTasks);
    }
    
    public ReportDTO getReportById(Long id) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        return reportOptional.map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Report not found: " + id));
    }
    
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReportDTO> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReportDTO> getReportsByType(ReportType type) {
        return reportRepository.findByType(type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private ReportDTO mapToDTO(Report report) {
        Double completionRate = 0.0;
        if (report.getTaskCount() != null && report.getTaskCount() > 0) {
            completionRate = report.getCompletedTaskCount() != null 
                    ? (double) report.getCompletedTaskCount() / report.getTaskCount() * 100
                    : 0.0;
        }
        
        return ReportDTO.builder()
                .id(report.getId())
                .name(report.getName())
                .description(report.getDescription())
                .createdAt(report.getCreatedAt())
                .userId(report.getUserId())
                .type(report.getType())
                .status(report.getStatus())
                .taskCount(report.getTaskCount())
                .completedTaskCount(report.getCompletedTaskCount())
                .completionRate(completionRate)
                .build();
    }
} 