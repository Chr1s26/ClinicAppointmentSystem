package com.clinic.appointment.service;

import com.clinic.appointment.dto.searchFilter.doctorDepartment.DoctorDepartmentDTO;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    public Doctor addDoctorToDepartment(DoctorDepartmentDTO dto){
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("doctor", dto.getDoctorId(), "departments", "/doctorDepartment", "Doctor not found"));

        List<Department> departments = dto.getDepartmentIds()
                .stream().map(id -> departmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("department", id, "doctor", "/doctorDepartment", "Department not found")))
                            .toList();

        for (Department dept : departments) {
            if (!doctor.getDepartments().contains(dept)) {
                doctor.addDepartment(dept);
            }
        }
        return doctorRepository.save(doctor);
    }
}
