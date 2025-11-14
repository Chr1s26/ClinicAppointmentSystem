package com.clinic.appointment.repository;

import com.clinic.appointment.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Optional<Department> findByDepartmentNameIgnoreCase(String departmentName);

    Optional<Department> findByDepartmentNameIgnoreCaseAndIdNot(String departmentName, Long id);
}
