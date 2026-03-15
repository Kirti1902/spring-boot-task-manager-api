package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.taskmanager.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        Task saved = repository.save(task);

        return TaskResponse.fromEntity(saved);
    }

    // GET ALL
    public List<TaskResponse> getAllTasks() {
        return repository.findAll()
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    // GET BY ID
    public TaskResponse getTaskById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        return TaskResponse.fromEntity(task);
    }

    // UPDATE
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        task.setTitle(request.title());
        task.setDescription(request.description());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        Task updated = repository.save(task);

        return TaskResponse.fromEntity(updated);
    }

    // DELETE
    public void deleteTask(Long id) {

    Task task = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

    task.setDeleted(true);

    repository.save(task);
    }

    public Page<TaskResponse> getAllTasks(Pageable pageable) {
    return repository.findAll(pageable)
            .map(TaskResponse::fromEntity);
    }

    public Page<TaskResponse> searchTasks(TaskStatus status, String title, Pageable pageable) {

    Page<Task> tasks;

    if (status != null && title != null) {
        tasks = repository.findByStatusAndTitleContainingIgnoreCase(status, title, pageable);
    }
    else if (status != null) {
        tasks = repository.findByStatus(status, pageable);
    }
    else if (title != null) {
        tasks = repository.findByTitleContainingIgnoreCase(title, pageable);
    }
    else {
        tasks = repository.findAll(pageable);
    }

    return tasks.map(TaskResponse::fromEntity);
    }
}