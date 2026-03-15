package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("REST API for managing tasks with pagination, search and filtering")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")
                        )
                );
    }
}