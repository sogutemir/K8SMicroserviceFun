package com.dijikent.reportingservice.repository;

import com.dijikent.reportingservice.model.Report;
import com.dijikent.reportingservice.model.ReportStatus;
import com.dijikent.reportingservice.model.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByUserId(Long userId);
    
    List<Report> findByStatus(ReportStatus status);
    
    List<Report> findByType(ReportType type);
    
    List<Report> findByUserIdAndStatus(Long userId, ReportStatus status);
    
    List<Report> findByUserIdAndType(Long userId, ReportType type);
    
    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
} 