package com.example.STUDENT.service;

import com.example.STUDENT.dto.DepartmentRequest;
import com.example.STUDENT.dto.DepartmentResponse;
import com.example.STUDENT.dto.StudentResponse;
import com.example.STUDENT.entity.Department;
import com.example.STUDENT.entity.Student;
import com.example.STUDENT.exception.DepartmentInUseException;
import com.example.STUDENT.exception.DepartmentNotFoundException;
import com.example.STUDENT.exception.DuplicateDepartmentCodeException;
import com.example.STUDENT.repository.DepartmentRepository;
import com.example.STUDENT.repository.StudentRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository, StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        return toResponse(findDepartment(id));
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String code = normalizeCode(request.getDepartmentCode());
        if (departmentRepository.existsByDepartmentCodeIgnoreCase(code)) {
            throw new DuplicateDepartmentCodeException();
        }

        Department department = new Department();
        copyRequest(request, department, code);
        return toResponse(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = findDepartment(id);
        String code = normalizeCode(request.getDepartmentCode());
        if (departmentRepository.existsByDepartmentCodeIgnoreCaseAndIdNot(code, id)) {
            throw new DuplicateDepartmentCodeException();
        }

        copyRequest(request, department, code);
        return toResponse(departmentRepository.save(department));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = findDepartment(id);
        if (studentRepository.existsByDepartment_Id(id)) {
            throw new DepartmentInUseException();
        }
        departmentRepository.delete(department);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByDepartment(Long id) {
        findDepartment(id);
        return studentRepository.findByDepartment_Id(id).stream()
                .map(this::toStudentResponse)
                .toList();
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    private void copyRequest(DepartmentRequest request, Department department, String code) {
        department.setDepartmentCode(code);
        department.setDepartmentName(request.getDepartmentName().trim());
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getDepartmentCode(),
                department.getDepartmentName());
    }

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getRegisterNo(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment() == null
                        ? student.getLegacyDepartmentName()
                        : student.getDepartment().getDepartmentName(),
                student.getYear(),
                student.getSemester(),
                student.getCreatedAt(),
                student.getUpdatedAt());
    }
}
