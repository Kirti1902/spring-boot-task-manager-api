package com.example.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ✅ Password Encoder (ONLY ONE in project)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Security Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // disable for testing (enable in production if needed)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()   // public endpoints
                .requestMatchers("/h2-console/**").permitAll() // for H2 DB (optional)
                .anyRequest().authenticated()                 // everything else secured
            )

            .headers(headers -> headers
                .frameOptions(frame -> frame.disable()) // for H2 console
            )

            .httpBasic(httpBasic -> {}); // basic auth (can switch to JWT later)

        return http.build();
    }
}