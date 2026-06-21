package com.example.taskmanager;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskPriority;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTaskSuccessfully() {

        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        TaskRequest request = new TaskRequest(
                "Learn Mockito",
                "Write first unit test",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDateTime.now().plusDays(1)
        );

        Task savedTask = new Task();
        savedTask.setId(100L);
        savedTask.setTitle(request.title());
        savedTask.setDescription(request.description());
        savedTask.setStatus(request.status());
        savedTask.setPriority(request.priority());
        savedTask.setDueDate(request.dueDate());
        savedTask.setUser(user);

        when(userService.getCurrentUser()).thenReturn(user);
        when(repository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskResponse response = taskService.createTask(request);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals("Learn Mockito", response.title());
        assertEquals("Write first unit test", response.description());
        assertEquals(TaskStatus.PENDING, response.status());
    }
}