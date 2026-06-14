package com.example.taskmanager.dto;

public record TaskStatsResponse(

        long totalTasks,
        long pendingTasks,
        long inProgressTasks,
        long completedTasks

) {
}