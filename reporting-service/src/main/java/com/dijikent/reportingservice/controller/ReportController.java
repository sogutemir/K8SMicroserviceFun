package com.dijikent.reportingservice.controller;

import com.dijikent.reportingservice.dto.CreateReportRequest;
import com.dijikent.reportingservice.dto.ReportDTO;
import com.dijikent.reportingservice.model.ReportType;
import com.dijikent.reportingservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportService reportService;
    
    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@RequestBody CreateReportRequest request) {
        ReportDTO report = reportService.createReport(request);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        ReportDTO report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDTO>> getReportsByUserId(@PathVariable Long userId) {
        List<ReportDTO> reports = reportService.getReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ReportDTO>> getReportsByType(@PathVariable ReportType type) {
        List<ReportDTO> reports = reportService.getReportsByType(type);
        return ResponseEntity.ok(reports);
    }
} 