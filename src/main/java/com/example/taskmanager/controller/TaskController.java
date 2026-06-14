package com.example.taskmanager.controller;

import com.example.taskmanager.dto.PageResponse;
import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.dto.TaskStatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // CREATE TASK
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new task")
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request
    ) {
        return service.createTask(request);
    }

    // GET TASKS
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get tasks with pagination and filtering")
    public PageResponse<TaskResponse> getTasks(

            @RequestParam(required = false)
            TaskStatus status,

            @RequestParam(required = false)
            String title,

            Pageable pageable
    ) {

        Page<TaskResponse> page =
                service.searchTasks(status, title, pageable);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // GET TASK BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get task by ID")
    public TaskResponse getTaskById(
            @PathVariable Long id
    ) {
        return service.getTaskById(id);
    }

    // UPDATE TASK
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Update task")
    public TaskResponse updateTask(

            @PathVariable Long id,

            @Valid @RequestBody
            TaskRequest request
    ) {

        return service.updateTask(id, request);
    }

    // DELETE TASK
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Delete task")
    public void deleteTask(
            @PathVariable Long id
    ) {
        service.deleteTask(id);
    }

    @GetMapping("/stats")
    public TaskStatsResponse getTaskStatistics() {
        return service.getTaskStatistics();
    }
}