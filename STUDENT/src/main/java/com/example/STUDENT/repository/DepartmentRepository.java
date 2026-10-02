package com.example.STUDENT.repository;

import com.example.STUDENT.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentCodeIgnoreCase(String departmentCode);

    boolean existsByDepartmentCodeIgnoreCaseAndIdNot(String departmentCode, Long id);

    Optional<Department> findByDepartmentCodeIgnoreCase(String departmentCode);

    List<Department> findByDepartmentNameIgnoreCase(String departmentName);

    Optional<Department> findFirstByDepartmentNameIgnoreCaseOrderByIdAsc(String departmentName);
}
