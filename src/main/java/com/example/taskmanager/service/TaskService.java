package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // ✅ Get logged-in username from JWT
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // CREATE
    public TaskResponse createTask(TaskRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(
                request.status() != null ? request.status() : TaskStatus.PENDING
        );
        task.setUser(user); // ✅ link task to user

        Task saved = repository.save(task);
        return TaskResponse.fromEntity(saved);
    }

    // GET BY ID (only own task)
    public TaskResponse getTaskById(Long id) {

        String username = getCurrentUsername();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id)
                );

        // ✅ ownership check
        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        return TaskResponse.fromEntity(task);
    }

    // UPDATE (only own task)
    public TaskResponse updateTask(Long id, TaskRequest request) {

        String username = getCurrentUsername();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id)
                );

        // ✅ ownership check
        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        Task updated = repository.save(task);
        return TaskResponse.fromEntity(updated);
    }

    // DELETE (only own task)
    public void deleteTask(Long id) {

        String username = getCurrentUsername();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id)
                );

        // ✅ ownership check
        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        repository.delete(task); // triggers @SQLDelete
    }

    // PAGINATED + FILTERED SEARCH (only own tasks)
    public Page<TaskResponse> searchTasks(
            TaskStatus status,
            String title,
            Pageable pageable
    ) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Task> tasks;

        if (status != null && title != null) {
            tasks = repository.findByUserAndStatusAndTitleContainingIgnoreCase(
                    user, status, title, pageable);
        }
        else if (status != null) {
            tasks = repository.findByUserAndStatus(user, status, pageable);
        }
        else if (title != null) {
            tasks = repository.findByUserAndTitleContainingIgnoreCase(
                    user, title, pageable);
        }
        else {
            tasks = repository.findByUser(user, pageable);
        }

        return tasks.map(TaskResponse::fromEntity);
    }
}