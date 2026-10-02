package com.example.STUDENT.dto;

public class DepartmentResponse {

    private Long id;
    private String departmentCode;
    private String departmentName;

    public DepartmentResponse(Long id, String departmentCode, String departmentName) {
        this.id = id;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
