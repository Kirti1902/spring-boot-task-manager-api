package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Role;
import com.example.taskmanager.entity.User;

public record UserResponse(

        Long id,
        String username,
        Role role

) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}