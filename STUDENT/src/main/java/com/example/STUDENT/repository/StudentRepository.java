package com.example.STUDENT.repository;

import com.example.STUDENT.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    @EntityGraph(attributePaths = "department")
    List<Student> findAll();

    @Override
    @EntityGraph(attributePaths = "department")
    Optional<Student> findById(Long id);

    boolean existsByRegisterNoIgnoreCase(String registerNo);

    boolean existsByRegisterNoIgnoreCaseAndIdNot(String registerNo, Long id);

    @EntityGraph(attributePaths = "department")
    List<Student> findByNameContainingIgnoreCaseOrRegisterNoContainingIgnoreCase(
            String name, String registerNo);

    @EntityGraph(attributePaths = "department")
    List<Student> findByDepartment_Id(Long departmentId);

    boolean existsByDepartment_Id(Long departmentId);

    @EntityGraph(attributePaths = "department")
    @Query("""
            SELECT s FROM Student s
            LEFT JOIN s.department d
            WHERE (:department IS NULL
                   OR LOWER(d.departmentName) = LOWER(:department)
                   OR LOWER(d.departmentCode) = LOWER(:department)
                   OR (d IS NULL
                       AND LOWER(s.legacyDepartmentName) = LOWER(:department)))
              AND (:year IS NULL OR s.year = :year)
              AND (:semester IS NULL OR s.semester = :semester)
            """)
    List<Student> filterStudents(
            @Param("department") String department,
            @Param("year") Integer year,
            @Param("semester") Integer semester);
}
