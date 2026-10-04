Student Management Backend

A RESTful Student Management Backend developed using Java, Spring Boot, Spring Data JPA, and MySQL.

The application provides APIs to manage students and departments, perform searches and filtering, validate input data, and maintain relationships between students and departments.

Features

- Student CRUD operations
- Department CRUD operations
- Student search by name
- Student filtering by department, year, and semester
- Input validation
- Duplicate register number validation
- Exception handling
- Student–Department relationship
- RESTful API architecture
- MySQL database integration
- JPA/Hibernate-based data persistence

Tech Stack

Technology| Purpose
Java| Backend programming
Spring Boot| Application framework
Spring Web| REST API development
Spring Data JPA| Database access
Hibernate| ORM
MySQL| Relational database
Maven| Dependency management

Project Architecture

Client / Postman
       ↓
REST Controller
       ↓
Service Layer
       ↓
Repository Layer
       ↓
MySQL Database

The project follows a layered backend architecture to separate API handling, business logic, database operations, and data models.

Student APIs

Method| Endpoint| Description
GET| "/api/students"| Get all students
GET| "/api/students/{id}"| Get student by ID
POST| "/api/students"| Create a student
PUT| "/api/students/{id}"| Update a student
DELETE| "/api/students/{id}"| Delete a student
GET| "/api/students/search?name=Arun"| Search students by name
GET| "/api/students?department=IT"| Filter by department
GET| "/api/students?year=3"| Filter by year
GET| "/api/students?semester=5"| Filter by semester

Department APIs

Method| Endpoint| Description
GET| "/api/departments"| Get all departments
GET| "/api/departments/{id}"| Get department by ID
POST| "/api/departments"| Create a department
PUT| "/api/departments/{id}"| Update a department
DELETE| "/api/departments/{id}"| Delete a department
GET| "/api/departments/{id}/students"| Get students belonging to a department

Validation & Error Handling

The backend includes:

- Required field validation
- Input data validation
- Duplicate register number checking
- Resource-not-found handling
- Invalid request handling
- Appropriate HTTP status codes
- Exception handling for API errors

Database

Database: "STUDENT"

The application uses MySQL for persistent storage and Spring Data JPA/Hibernate for database operations.

Main Entities

Department
    │
    └── Students

A department can have multiple students, establishing a Department–Student relationship.

API Testing

The APIs can be tested using Postman.

Example:

GET http://localhost:8080/api/students

Create a student:

POST http://localhost:8080/api/students
Content-Type: application/json

Example request body:

{
  "name": "Arun",
  "registerNumber": "REG001",
  "department": "IT",
  "year": 3,
  "semester": 5
}

How to Run

1. Clone the repository

git clone <your-repository-url>
cd <project-folder>

2. Create the MySQL database

CREATE DATABASE STUDENT;

3. Configure MySQL

Update the database credentials in:

src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:mysql://localhost:3306/STUDENT
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

4. Run the application

Using Maven:

mvn spring-boot:run

The application will start at:

http://localhost:8080

Project Structure

src
└── main
    ├── java
    │   └── ... 
    │       ├── controller
    │       ├── service
    │       ├── repository
    │       ├── entity
    │       └── exception
    │
    └── resources
        └── application.properties

Future Improvements

- JWT-based authentication and authorization
- Pagination and sorting
- Swagger/OpenAPI documentation
- Role-based access control
- Unit and integration testing
- Docker containerization
- Frontend integration

Author

Mohan Babu

B.Tech – Artificial Intelligence & Data Science

---

Project: Student Management Backend
Backend: Java + Spring Boot
Database: MySQL
