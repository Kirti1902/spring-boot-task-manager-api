# Task Manager API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)
![Build](https://img.shields.io/badge/build-maven-orange)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

A **Spring Boot REST API** for managing tasks with authentication, pagination, filtering, and API documentation.

This project demonstrates **clean backend architecture and best practices** used in modern Java backend systems.

---

# Features

* User registration and login
* JWT based authentication
* Role-based authorization (USER / ADMIN)
* Create, update, delete tasks
* Pagination support
* Filtering and searching tasks
* Task statistics dashboard
* Admin user management
* Audit logging for task operations
* Swagger API documentation
* Global exception handling
* DTO based request/response design
* Layered architecture
* Unit testing with JUnit and Mockito

---

# Tech Stack

| Technology        | Purpose                          |
| ----------------- | -------------------------------- |
| Java 21           | Programming language             |
| Spring Boot       | Backend framework                |
| Spring Security   | Authentication & authorization   |
| JWT               | Secure token authentication      |
| Spring Data JPA   | Database interaction             |
| Hibernate         | ORM framework                    |
| H2 Database       | Lightweight development database |
| Swagger / OpenAPI | API documentation                |
| Maven             | Build tool                       |

---

# Project Architecture

The project follows **layered architecture**:

Controller → Service → Repository → Database

### Layers

**Controller**

Handles HTTP requests and responses.

**Service**

Contains business logic.

**Repository**

Handles database operations using Spring Data JPA.

**Entity**

JPA database models.

**DTO**

Request and response objects used to transfer data.

**Security**

JWT authentication and Spring Security configuration.

**Exception**

Global exception handling for consistent API responses.

---

# Project Structure

```
src/main/java/com/example/taskmanager

controller/
    TaskController
    AuthController
    AdminController
    HomeController

service/
    TaskService
    AuthService
    AdminService
    AuditService
    UserService

repository/
    TaskRepository
    UserRepository
    AuditLogRepository

entity/
    Task
    User
    AuditLog
    Role
    TaskStatus
    TaskPriority

dto/
    TaskRequest
    TaskResponse
    AuthRequest
    AuthResponse
    UserResponse
    TaskStatsResponse
    AuditLogResponse

security/
    JwtUtil
    JwtFilter
    SecurityConfig

exception/
    GlobalExceptionHandler
```

---

# API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

You can test all APIs directly from the browser.

---

# Authentication

The API uses **JWT (JSON Web Token)** authentication.

Users must register or login to obtain a token.

---

# Authentication APIs

## Register

POST /auth/register

Request

```
{
  "username": "admin",
  "password": "admin123"
}
```

Response

```
{
  "token": "JWT_TOKEN"
}
```

---

# Admin APIs

| Method | Endpoint | Description |
|----------|------------|-------------|
| GET | /admin/users | Get all users |
| GET | /admin/users/{id} | Get user by ID |
| DELETE | /admin/users/{id} | Delete user |
| GET | /admin/audit-logs | View audit logs |

## Login

POST /auth/login

Request

```
{
  "username": "admin",
  "password": "admin123"
}
```

Response

```
{
  "token": "JWT_TOKEN"
}
```

---

# Task Statistics

Endpoint:

GET /tasks/stats

Response:

{
  "total": 15,
  "pending": 5,
  "inProgress": 4,
  "completed": 6
}

---

# Using the Token

After login, include the token in request headers.

```
Authorization: Bearer YOUR_TOKEN
```

Example:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

# Task APIs

| Method | Endpoint    | Description                   |
| ------ | ----------- | ----------------------------- |
| GET    | /tasks      | Get all tasks with pagination |
| GET    | /tasks/{id} | Get task by ID                |
| POST   | /tasks      | Create new task               |
| PUT    | /tasks/{id} | Update task                   |
| DELETE | /tasks/{id} | Delete task                   |

---

# Pagination Example

```
GET /tasks?page=0&size=5
```

Query parameters

| Parameter | Description                |
| --------- | -------------------------- |
| page      | page number                |
| size      | number of records per page |

---

# Running the Project

## 1 Clone the repository

```
git clone <repository-url>
```

## 2 Navigate to project folder

```
cd taskmanager
```

## 3 Build project

```
mvn clean install
```

## 4 Run the application

```
mvn spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

---

# Database

This project uses **H2 Database** for development.

### H2 Console

```
http://localhost:8080/h2-console
```

Connection details

```
JDBC URL: jdbc:h2:file:./data/taskdb
Username: sa
Password: (leave empty)
```

---

# Example Task Request

```
POST /tasks
```

```
{
  "title": "Complete backend project",
  "description": "Finish Spring Boot Task Manager API",
  "status": "PENDING"
}
```

---

# Audit Logging

The system automatically records important user actions.

Examples:

* Created task #1
* Updated task #1
* Deleted task #1

Each log stores:

* Username
* Action performed
* Timestamp

Admins can view all audit logs through:

GET /admin/audit-logs

---

# Future Improvements

Possible enhancements:

* PostgreSQL database
* Docker support
* Integration testing
* CI/CD pipeline
* Logging and monitoring
* Cloud deployment

---

# Author

Backend project developed using Spring Boot to demonstrate REST API development, authentication, and backend architecture.

---

# License

This project is licensed under the MIT License.
