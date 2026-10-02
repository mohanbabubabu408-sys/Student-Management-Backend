# Student Management Backend

A Student Management REST API built using Java, Spring Boot, Spring Data JPA, and MySQL.

## Technologies

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## Features

- Student CRUD operations
- Student search
- Student filtering
- Input validation
- Duplicate register number validation
- Exception handling
- Department management
- Department-Student relationship

## Student APIs

| Method | Endpoint |
|---|---|
| GET | `/api/students` |
| GET | `/api/students/{id}` |
| POST | `/api/students` |
| PUT | `/api/students/{id}` |
| DELETE | `/api/students/{id}` |
| GET | `/api/students/search?name=Arun` |
| GET | `/api/students?department=IT` |
| GET | `/api/students?year=3` |
| GET | `/api/students?semester=5` |

## Department APIs

| Method | Endpoint |
|---|---|
| GET | `/api/departments` |
| GET | `/api/departments/{id}` |
| POST | `/api/departments` |
| PUT | `/api/departments/{id}` |
| DELETE | `/api/departments/{id}` |
| GET | `/api/departments/{id}/students` |

## Database

Database: STUDENT 
MySQL is used as the database

## Run

Configure your MySQL credentials in `application.properties`.

Then run:
https://localhost:8080
mvn spring-boot:run

```text
STUDENT
