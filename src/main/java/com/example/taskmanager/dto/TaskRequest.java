package com.example.taskmanager.dto;

import com.example.taskmanager.entity.TaskPriority;
import com.example.taskmanager.entity.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100,
                message = "Title must be between 3 and 100 characters")
        String title,

        @Size(max = 500,
                message = "Description must not exceed 500 characters")
        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime dueDate

) {
}