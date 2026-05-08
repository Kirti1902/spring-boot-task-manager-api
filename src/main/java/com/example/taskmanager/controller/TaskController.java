package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.PageResponse;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.entity.TaskStatus;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task API", description = "Operations related to task management")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // CREATE
    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request
    ) {
        TaskResponse response = service.createTask(request);
        return ResponseEntity.status(201).body(response);
    }

    // GET ALL with pagination, search, filter
    @Operation(summary = "Get tasks with pagination, search and filtering")
    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String title,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<TaskResponse> pageResult = service.searchTasks(status, title, pageable);

        PageResponse<TaskResponse> response = new PageResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    // GET BY ID
    @Operation(summary = "Get a task by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTaskById(id));
    }

    // UPDATE
    @Operation(summary = "Update an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(service.updateTask(id, request));
    }

    // DELETE
    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build(); // 204
    }
}