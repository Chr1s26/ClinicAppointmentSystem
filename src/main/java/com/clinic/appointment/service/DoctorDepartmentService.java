package com.clinic.appointment.service;

import com.clinic.appointment.dto.DepartmentDTO;
import com.clinic.appointment.dto.DepartmentProjection;
import com.clinic.appointment.dto.DepartmentResponse;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorDepartmentService {

    private final DoctorService doctorService ;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;


    public Doctor addDoctorToDepartment(Long doctorId, Long departmentId){
        Doctor doctor = doctorService.findDoctorById(doctorId);
        Department department = departmentService.findDepartmentById(departmentId);
        doctor.addDepartment(department);
        return doctorService.updateDoctor(doctorId,doctor);
    }

    public DepartmentResponse getAllDepartmentsByDoctorId(Long doctorId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<DepartmentProjection> page = departmentRepository.findAllWithDoctorJoinedStatus(doctorId, pageable);

        List<DepartmentDTO> dtos = page.getContent().stream().map(proj -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setId(proj.getId());
            dto.setDepartmentName(proj.getDepartmentName());
            dto.setDepartmentDescription(proj.getDepartmentDescription());
            dto.setCreatedAt(proj.getCreatedAt());
            dto.setUpdatedAt(proj.getUpdatedAt());
            dto.setJoined(Boolean.TRUE.equals(proj.getIsJoined()));
            return dto;
        }).collect(Collectors.toList());

        DepartmentResponse response = new DepartmentResponse();
        response.setDepartments(dtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }


}
