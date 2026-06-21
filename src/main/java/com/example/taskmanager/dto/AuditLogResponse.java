package com.example.taskmanager.dto;

import com.example.taskmanager.entity.AuditLog;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String username,
        String action,
        LocalDateTime timestamp
) {

    public static AuditLogResponse fromEntity(
            AuditLog log
    ) {
        return new AuditLogResponse(
                log.getId(),
                log.getUsername(),
                log.getAction(),
                log.getTimestamp()
        );
    }
}