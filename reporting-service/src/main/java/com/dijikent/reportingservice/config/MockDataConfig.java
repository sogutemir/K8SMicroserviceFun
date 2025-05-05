package com.dijikent.reportingservice.config;

import com.dijikent.reportingservice.dto.TaskDTO;
import com.dijikent.reportingservice.dto.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MockDataConfig {

    @Bean
    @Profile("dev")
    public Map<Long, UserDTO> mockUserData() {
        Map<Long, UserDTO> mockUsers = new ConcurrentHashMap<>();
        mockUsers.put(1L, new UserDTO(1L, "Ahmet Yılmaz", "ahmet.yilmaz@example.com"));
        mockUsers.put(2L, new UserDTO(2L, "Ayşe Demir", "ayse.demir@example.com"));
        mockUsers.put(3L, new UserDTO(3L, "Mehmet Kara", "mehmet.kara@example.com"));
        mockUsers.put(4L, new UserDTO(4L, "Zeynep Ak", "zeynep.ak@example.com"));
        mockUsers.put(5L, new UserDTO(5L, "Mustafa Çelik", "mustafa.celik@example.com"));
        return mockUsers;
    }

    @Bean
    @Profile("dev")
    public Map<Long, TaskDTO> mockTaskData() {
        Map<Long, TaskDTO> mockTasks = new ConcurrentHashMap<>();
        mockTasks.put(1L, new TaskDTO(1L, "Raporları hazırla", true, 1L));
        mockTasks.put(2L, new TaskDTO(2L, "Müşteri toplantısı", false, 1L));
        mockTasks.put(3L, new TaskDTO(3L, "Sunum dosyasını güncelle", true, 1L));
        mockTasks.put(4L, new TaskDTO(4L, "Proje planını oluştur", false, 2L));
        mockTasks.put(5L, new TaskDTO(5L, "E-postaları yanıtla", true, 2L));
        mockTasks.put(6L, new TaskDTO(6L, "Tasarım dokümanını tamamla", false, 3L));
        mockTasks.put(7L, new TaskDTO(7L, "Kod incelemesi yap", true, 3L));
        mockTasks.put(8L, new TaskDTO(8L, "Sprint planlaması", true, 3L));
        mockTasks.put(9L, new TaskDTO(9L, "Hata düzeltmesi", false, 4L));
        mockTasks.put(10L, new TaskDTO(10L, "Dokümantasyon", true, 4L));
        mockTasks.put(11L, new TaskDTO(11L, "Bütçe analizi", false, 5L));
        mockTasks.put(12L, new TaskDTO(12L, "Performans değerlendirmesi", true, 5L));
        mockTasks.put(13L, new TaskDTO(13L, "Ekip toplantısı", true, 5L));
        mockTasks.put(14L, new TaskDTO(14L, "Kaynak planlama", false, 1L));
        mockTasks.put(15L, new TaskDTO(15L, "Yıllık plan", true, 2L));
        return mockTasks;
    }

    @Bean
    @Profile("dev")
    public Map<Long, List<TaskDTO>> mockTasksByUserIdData() {
        Map<Long, List<TaskDTO>> tasksByUserId = new ConcurrentHashMap<>();
        Map<Long, TaskDTO> allTasks = mockTaskData();
        
        for (TaskDTO task : allTasks.values()) {
            tasksByUserId.computeIfAbsent(task.getUserId(), k -> new ArrayList<>()).add(task);
        }
        
        return tasksByUserId;
    }
} 