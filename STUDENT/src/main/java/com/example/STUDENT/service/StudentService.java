package com.example.STUDENT.service;

import com.example.STUDENT.dto.StudentRequest;
import com.example.STUDENT.dto.StudentResponse;
import com.example.STUDENT.entity.Department;
import com.example.STUDENT.entity.Student;
import com.example.STUDENT.exception.DuplicateRegisterNumberException;
import com.example.STUDENT.exception.InvalidStudentDepartmentException;
import com.example.STUDENT.exception.StudentNotFoundException;
import com.example.STUDENT.repository.DepartmentRepository;
import com.example.STUDENT.repository.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentService(
            StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long id) {
        return toResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByRegisterNoIgnoreCase(request.getRegisterNo())) {
            throw new DuplicateRegisterNumberException();
        }

        Student student = new Student();
        copyRequest(request, student);
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = findStudent(id);
        if (studentRepository.existsByRegisterNoIgnoreCaseAndIdNot(request.getRegisterNo(), id)) {
            throw new DuplicateRegisterNumberException();
        }

        copyRequest(request, student);
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    public List<StudentResponse> searchStudents(String name, String registerNo) {
        String nameTerm = StringUtils.hasText(name) ? name : registerNo;
        String registerNoTerm = StringUtils.hasText(registerNo) ? registerNo : name;

        if (!StringUtils.hasText(nameTerm) && !StringUtils.hasText(registerNoTerm)) {
            return List.of();
        }

        return studentRepository
                .findByNameContainingIgnoreCaseOrRegisterNoContainingIgnoreCase(
                        nameTerm, registerNoTerm)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> filterStudents(String department, Integer year, Integer semester) {
        String departmentFilter = StringUtils.hasText(department) ? department : null;
        return studentRepository.filterStudents(departmentFilter, year, semester).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private void copyRequest(StudentRequest request, Student student) {
        student.setRegisterNo(request.getRegisterNo().trim());
        student.setName(request.getName().trim());
        student.setEmail(request.getEmail().trim());
        student.setPhone(request.getPhone().trim());
        String departmentValue = request.getDepartment().trim();
        Department department = departmentRepository
                .findByDepartmentCodeIgnoreCase(departmentValue)
                .orElseGet(() -> findDepartmentByName(departmentValue));
        student.setDepartment(department);
        student.setLegacyDepartmentName(department.getDepartmentName());
        student.setYear(request.getYear());
        student.setSemester(request.getSemester());
    }

    private Department findDepartmentByName(String departmentName) {
        List<Department> matches =
                departmentRepository.findByDepartmentNameIgnoreCase(departmentName);
        if (matches.isEmpty()) {
            throw new InvalidStudentDepartmentException(
                    "Department must match an existing department code or name");
        }
        if (matches.size() > 1) {
            throw new InvalidStudentDepartmentException(
                    "Department name is ambiguous; use the department code");
        }
        return matches.get(0);
    }

    private StudentResponse toResponse(Student student) {
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
