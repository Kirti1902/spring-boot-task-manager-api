package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskStatsResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.exception.UnauthorizedException;
import com.example.taskmanager.repository.TaskRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserService userService;
    private final AuditService auditService;

    public TaskService(
            TaskRepository repository,
            UserService userService,
            AuditService auditService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.auditService = auditService;
    }

    // CREATE
    public TaskResponse createTask(TaskRequest request) {

        User currentUser = userService.getCurrentUser();

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        task.setStatus(
                request.status() != null
                        ? request.status()
                        : TaskStatus.PENDING
        );
        task.setUser(currentUser);

        Task saved = repository.save(task);

        auditService.log(
                currentUser.getUsername(),
                "Created task #" + saved.getId()
        );

        return TaskResponse.fromEntity(saved);
    }

    // GET BY ID
    public TaskResponse getTaskById(Long id) {

        User currentUser = userService.getCurrentUser();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id
                        )
                );

        if (!task.getUser().getId().equals(currentUser.getId())
                && !userService.isAdmin()) {

            throw new UnauthorizedException(
                    "You are not allowed to access this task"
            );
        }

        return TaskResponse.fromEntity(task);
    }

    // UPDATE
    public TaskResponse updateTask(
            Long id,
            TaskRequest request
    ) {

        User currentUser = userService.getCurrentUser();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id
                        )
                );

        if (!task.getUser().getId().equals(currentUser.getId())
                && !userService.isAdmin()) {

            throw new UnauthorizedException(
                    "You are not allowed to update this task"
            );
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        Task updated = repository.save(task);

        auditService.log(
                currentUser.getUsername(),
                "Updated task #" + updated.getId()
        );

        return TaskResponse.fromEntity(updated);
    }

    // DELETE
    public void deleteTask(Long id) {

        User currentUser = userService.getCurrentUser();

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id
                        )
                );

        if (!task.getUser().getId().equals(currentUser.getId())
                && !userService.isAdmin()) {

            throw new UnauthorizedException(
                    "You are not allowed to delete this task"
            );
        }

        auditService.log(
                currentUser.getUsername(),
                "Deleted task #" + task.getId()
        );

        repository.delete(task);
    }

    // SEARCH
    public Page<TaskResponse> searchTasks(
            TaskStatus status,
            String title,
            Pageable pageable
    ) {

        User currentUser = userService.getCurrentUser();

        // ADMIN → ALL TASKS
        if (userService.isAdmin()) {

            if (status != null && title != null) {
                return repository
                        .findByStatusAndTitleContainingIgnoreCase(
                                status,
                                title,
                                pageable
                        )
                        .map(TaskResponse::fromEntity);
            }
            else if (status != null) {
                return repository
                        .findByStatus(status, pageable)
                        .map(TaskResponse::fromEntity);
            }
            else if (title != null) {
                return repository
                        .findByTitleContainingIgnoreCase(
                                title,
                                pageable
                        )
                        .map(TaskResponse::fromEntity);
            }
            else {
                return repository
                        .findAll(pageable)
                        .map(TaskResponse::fromEntity);
            }
        }

        // USER → ONLY OWN TASKS
        if (status != null && title != null) {

            return repository
                    .findByUserAndStatusAndTitleContainingIgnoreCase(
                            currentUser,
                            status,
                            title,
                            pageable
                    )
                    .map(TaskResponse::fromEntity);

        } else if (status != null) {

            return repository
                    .findByUserAndStatus(
                            currentUser,
                            status,
                            pageable
                    )
                    .map(TaskResponse::fromEntity);

        } else if (title != null) {

            return repository
                    .findByUserAndTitleContainingIgnoreCase(
                            currentUser,
                            title,
                            pageable
                    )
                    .map(TaskResponse::fromEntity);

        } else {

            return repository
                    .findByUser(
                            currentUser,
                            pageable
                    )
                    .map(TaskResponse::fromEntity);
        }
    }

    // TASK STATISTICS
    public TaskStatsResponse getTaskStatistics() {

        User currentUser = userService.getCurrentUser();

        if (userService.isAdmin()) {

            long total = repository.count();

            long pending =
                    repository.countByStatus(TaskStatus.PENDING);

            long inProgress =
                    repository.countByStatus(TaskStatus.IN_PROGRESS);

            long completed =
                    repository.countByStatus(TaskStatus.COMPLETED);

            return new TaskStatsResponse(
                    total,
                    pending,
                    inProgress,
                    completed
            );
        }

        long total =
                repository.countByUser(currentUser);

        long pending =
                repository.countByUserAndStatus(
                        currentUser,
                        TaskStatus.PENDING
                );

        long inProgress =
                repository.countByUserAndStatus(
                        currentUser,
                        TaskStatus.IN_PROGRESS
                );

        long completed =
                repository.countByUserAndStatus(
                        currentUser,
                        TaskStatus.COMPLETED
                );

        return new TaskStatsResponse(
                total,
                pending,
                inProgress,
                completed
        );
    }
}