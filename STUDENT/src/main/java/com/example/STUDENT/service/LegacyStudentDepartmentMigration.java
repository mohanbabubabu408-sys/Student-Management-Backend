package com.example.STUDENT.service;

import com.example.STUDENT.entity.Department;
import com.example.STUDENT.repository.DepartmentRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LegacyStudentDepartmentMigration implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final DepartmentRepository departmentRepository;

    public LegacyStudentDepartmentMigration(
            JdbcTemplate jdbcTemplate, DepartmentRepository departmentRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<LegacyDepartment> legacyDepartments = jdbcTemplate.query("""
                SELECT MIN(id) AS student_id, department
                FROM students
                WHERE department_id IS NULL
                  AND department IS NOT NULL
                  AND TRIM(department) <> ''
                GROUP BY department
                """, this::toLegacyDepartment);

        for (LegacyDepartment legacy : legacyDepartments) {
            Department department = departmentRepository
                    .findByDepartmentCodeIgnoreCase(legacy.name())
                    .or(() -> departmentRepository
                            .findFirstByDepartmentNameIgnoreCaseOrderByIdAsc(legacy.name()))
                    .orElseGet(() -> createLegacyDepartment(legacy));
            jdbcTemplate.update(
                    "UPDATE students SET department_id = ? "
                            + "WHERE department = ? AND department_id IS NULL",
                    department.getId(),
                    legacy.name());
        }
    }

    private LegacyDepartment toLegacyDepartment(ResultSet resultSet, int rowNumber)
            throws SQLException {
        return new LegacyDepartment(
                resultSet.getLong("student_id"),
                resultSet.getString("department"));
    }

    private Department createLegacyDepartment(LegacyDepartment legacy) {
        Department department = new Department();
        String baseCode =
                "LEGACY-" + Long.toString(legacy.studentId(), 36).toUpperCase(Locale.ROOT);
        String code = baseCode;
        int suffix = 1;
        while (departmentRepository.existsByDepartmentCodeIgnoreCase(code)) {
            code = baseCode + "-" + suffix++;
        }
        department.setDepartmentCode(code);
        department.setDepartmentName(legacy.name());
        return departmentRepository.save(department);
    }

    private record LegacyDepartment(long studentId, String name) {
    }
}
