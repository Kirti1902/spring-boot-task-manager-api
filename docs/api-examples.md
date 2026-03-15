# API Examples

## Register User

POST /auth/register

{
  "username": "admin",
  "password": "admin123"
}

---

## Login

POST /auth/login

{
  "username": "admin",
  "password": "admin123"
}

---

## Create Task

POST /tasks

{
  "title": "Finish project",
  "description": "Complete task manager API",
  "status": "PENDING"
}

---

## Get Tasks

GET /tasks?page=0&size=5