package com.example.taskmanager.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = 10; // seconds

    private final Map<String, UserRequest> requestMap = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {

        UserRequest userRequest =
                requestMap.getOrDefault(key, new UserRequest(0, Instant.now()));

        if (Instant.now().isAfter(userRequest.timestamp.plusSeconds(TIME_WINDOW))) {
            userRequest = new UserRequest(1, Instant.now());
        } else {
            userRequest.count++;
        }

        requestMap.put(key, userRequest);

        return userRequest.count <= MAX_REQUESTS;
    }

    static class UserRequest {
        int count;
        Instant timestamp;

        UserRequest(int count, Instant timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}