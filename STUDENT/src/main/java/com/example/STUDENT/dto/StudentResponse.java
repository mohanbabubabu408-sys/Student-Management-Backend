package com.example.STUDENT.dto;

import java.time.LocalDateTime;

public class StudentResponse {

    private Long id;
    private String registerNo;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer year;
    private Integer semester;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentResponse() {
    }

    public StudentResponse(Long id, String registerNo, String name, String email, String phone,
            String department, Integer year, Integer semester,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.registerNo = registerNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.year = year;
        this.semester = semester;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getSemester() {
        return semester;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
