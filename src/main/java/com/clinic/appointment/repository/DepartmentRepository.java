package com.clinic.appointment.repository;

import com.clinic.appointment.dto.DepartmentDTO;
import com.clinic.appointment.dto.DepartmentProjection;
import com.clinic.appointment.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    @Query(value = """
    SELECT d.id, 
           d.department_name AS departmentName, 
           d.department_description AS departmentDescription,
           d.created_at AS createdAt, 
           d.updated_at AS updatedAt,
           CASE 
               WHEN EXISTS (
                   SELECT 1 
                   FROM doctor_department dd 
                   WHERE dd.dept_id = d.id AND dd.doctor_id = :doctorId
               ) 
               THEN true 
               ELSE false 
           END AS isJoined
    FROM departments d
    """,
            countQuery = """
    SELECT COUNT(*) 
    FROM departments d
    """,
            nativeQuery = true)
    Page<DepartmentProjection> findAllWithDoctorJoinedStatus(@Param("doctorId") Long doctorId, Pageable pageable);

}
