package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                .info(new Info()
                        .title("Task Manager API")
                        .description("""
                                REST API for Task Management System.

                                Features:
                                - JWT Authentication
                                - Role-based Authorization
                                - Task CRUD Operations
                                - Pagination & Sorting
                                - Search & Filtering
                                - Due Dates & Priorities
                                - Global Exception Handling
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kirti Chaudhary")
                                .email("kirti@example.com")
                        )
                        .license(new License()
                                .name("MIT License")
                        )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter JWT token in format: Bearer <token>")
                                )
                );
    }
}