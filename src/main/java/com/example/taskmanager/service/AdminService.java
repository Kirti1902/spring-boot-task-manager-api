package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.UnauthorizedException;
import com.example.taskmanager.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminService(
            UserRepository userRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private void verifyAdmin() {

        if (!userService.isAdmin()) {
            throw new UnauthorizedException(
                    "Admin access required"
            );
        }
    }

    public List<UserResponse> getAllUsers() {

        verifyAdmin();

        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse getUserById(Long id) {

        verifyAdmin();

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return UserResponse.fromEntity(user);
    }

    public void deleteUser(Long id) {

        verifyAdmin();

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        userRepository.delete(user);
    }
}