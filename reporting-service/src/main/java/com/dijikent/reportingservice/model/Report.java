package com.dijikent.reportingservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "reports")
public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    private ReportType type;
    
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    
    private Integer taskCount;
    
    private Integer completedTaskCount;
    
    public Report() {
    }

    public Report(Long id, String name, String description, LocalDateTime createdAt, 
                  Long userId, ReportType type, ReportStatus status,
                  Integer taskCount, Integer completedTaskCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.taskCount = taskCount;
        this.completedTaskCount = completedTaskCount;
    }

    @Override
    public String toString() {
        return "Report{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", createdAt=" + createdAt +
               ", userId=" + userId +
               ", type=" + type +
               ", status=" + status +
               ", taskCount=" + taskCount +
               ", completedTaskCount=" + completedTaskCount +
               '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Report report = (Report) o;
        return getId() != null && Objects.equals(getId(), report.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
} 