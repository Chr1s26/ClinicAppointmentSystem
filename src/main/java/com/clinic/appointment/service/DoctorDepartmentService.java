package com.clinic.appointment.service;

import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorDepartmentService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public Doctor addDoctorToDepartment(Long doctorId, Long departmentId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        Department department = departmentRepository.findById(departmentId).get();
        doctor.getDepartments().add(department);
        return doctorRepository.save(doctor);
    }
}
