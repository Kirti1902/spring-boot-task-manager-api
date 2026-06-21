package com.example.taskmanager.controller;

import com.example.taskmanager.dto.AuditLogResponse;
import com.example.taskmanager.dto.UserResponse;
import com.example.taskmanager.service.AdminService;
import com.example.taskmanager.service.AuditService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;
    private final AuditService auditService;

    public AdminController(
            AdminService service,
            AuditService auditService
    ) {
        this.service = service;
        this.auditService = auditService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(
            @PathVariable Long id
    ) {
        return service.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(
            @PathVariable Long id
    ) {
        service.deleteUser(id);
    }

    @GetMapping("/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLogResponse> getAuditLogs() {
        return auditService.getAllLogs();
    }
}