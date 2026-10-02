package com.example.STUDENT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequest {

    @NotBlank(message = "Department code is required")
    @Size(max = 30, message = "Department code must be at most 30 characters")
    private String departmentCode;

    @NotBlank(message = "Department name is required")
    @Size(max = 80, message = "Department name must be at most 80 characters")
    private String departmentName;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
