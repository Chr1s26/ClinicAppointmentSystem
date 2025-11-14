package com.clinic.appointment.service;

import com.clinic.appointment.dto.department.DepartmentCreateDTO;
import com.clinic.appointment.dto.department.DepartmentDTO;
import com.clinic.appointment.dto.department.DepartmentUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.NotFoundException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentDTO createDepartment(DepartmentCreateDTO dto, AppUser user) {

        departmentRepository.findByDepartmentNameIgnoreCase(dto.getDepartmentName())
                .ifPresent(d -> {
                    throw new DuplicateException("Department name already exists.");
                });

        Department department = Department.builder()
                .departmentName(dto.getDepartmentName())
                .departmentDescription(dto.getDepartmentDescription())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .createdBy(user)
                .updatedBy(user)
                .status("ACTIVE")
                .build();

        departmentRepository.save(department);

        return toDTO(department);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentUpdateDTO dto, AppUser user) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found."));

        departmentRepository.findByDepartmentNameIgnoreCaseAndIdNot(dto.getDepartmentName(), id)
                .ifPresent(d -> {
                    throw new DuplicateException("Another department with same name already exists.");
                });

        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentDescription(dto.getDepartmentDescription());
        department.setUpdatedAt(LocalDate.now());
        department.setUpdatedBy(user);

        departmentRepository.save(department);

        return toDTO(department);
    }

    public DepartmentDTO findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found."));
        return toDTO(department);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found."));

        department.setStatus("DELETE");
        departmentRepository.save(department);
    }

    private DepartmentDTO toDTO(Department d) {
        return new DepartmentDTO(
                d.getId(),
                d.getDepartmentName(),
                d.getDepartmentDescription(),
                d.getStatus() != null ? d.getStatusEnum() : null,
                d.getCreatedBy(),
                d.getUpdatedBy()
        );
    }
}
