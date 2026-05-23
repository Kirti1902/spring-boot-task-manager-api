package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskPriority;
import com.example.taskmanager.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(

        Long id,

        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime dueDate,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long userId,

        String username

) {

    public static TaskResponse fromEntity(Task task) {

        return new TaskResponse(

                task.getId(),

                task.getTitle(),

                task.getDescription(),

                task.getStatus(),

                task.getPriority(),

                task.getDueDate(),

                task.getCreatedAt(),

                task.getUpdatedAt(),

                task.getUser() != null
                        ? task.getUser().getId()
                        : null,

                task.getUser() != null
                        ? task.getUser().getUsername()
                        : null
        );
    }
}