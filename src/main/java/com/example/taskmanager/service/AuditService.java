package com.example.taskmanager.service;

import com.example.taskmanager.dto.AuditLogResponse;
import com.example.taskmanager.entity.AuditLog;
import com.example.taskmanager.repository.AuditLogRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(
            AuditLogRepository repository
    ) {
        this.repository = repository;
    }

    public void log(
            String username,
            String action
    ) {

        AuditLog log = new AuditLog();

        log.setUsername(username);
        log.setAction(action);

        repository.save(log);
    }

    public List<AuditLogResponse> getAllLogs() {

        return repository
                .findAllByOrderByTimestampDesc()
                .stream()
                .map(AuditLogResponse::fromEntity)
                .toList();
    }
}