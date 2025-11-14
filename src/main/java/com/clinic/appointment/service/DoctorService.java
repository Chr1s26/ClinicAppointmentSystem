package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctor.DoctorCreateDTO;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DoctorDTO findById(Long id) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return modelMapper.map(d, DoctorDTO.class);
    }

    @Transactional
    public DoctorDTO create(DoctorCreateDTO dto, AppUser currentUser) {

        // phone duplicate
        doctorRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(d -> { throw new DuplicateException("Phone already used by another doctor"); });

        // appuser email duplicate (if appUser exists check by service layer upstream)
        // we assume AppUser exists and ownership managed elsewhere

        // build doctor
        Doctor doc = new Doctor();
        doc.setName(dto.getName());
        doc.setPhone(dto.getPhone());
        doc.setAddress(dto.getAddress());
        doc.setDateOfBirth(dto.getDateOfBirth());
        doc.setGenderType(dto.getGenderType());
        doc.setCreatedAt(LocalDate.now());
        doc.setUpdatedAt(LocalDate.now());
        doc.setCreatedBy(currentUser);
        doc.setUpdatedBy(currentUser);
        doc.setStatus("ACTIVE");

        // set departments
        Set<Department> depts = dto.getDepartmentIds().stream()
                .map(id -> departmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id)))
                .collect(Collectors.toSet());
        doc.setDepartments(depts);

        // set appUser link (only id provided in DTO), lazily attach minimal AppUser
        AppUser au = new AppUser();
        au.setId(dto.getAppUserId());
        doc.setAppUser(au);

        Doctor saved = doctorRepository.save(doc);
        return modelMapper.map(saved, DoctorDTO.class);
    }

    @Transactional
    public DoctorDTO update(Long id, DoctorUpdateDTO dto, AppUser currentUser) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // check phone duplicate
        doctorRepository.findByPhoneIgnoreCaseAndIdNot(dto.getPhone(), id)
                .ifPresent(d -> { throw new DuplicateException("Phone already used by another doctor"); });

        doc.setName(dto.getName());
        doc.setPhone(dto.getPhone());
        doc.setAddress(dto.getAddress());
        doc.setDateOfBirth(dto.getDateOfBirth());
        doc.setGenderType(dto.getGenderType());
        doc.setUpdatedAt(LocalDate.now());
        doc.setUpdatedBy(currentUser);

        Set<Department> depts = dto.getDepartmentIds().stream()
                .map(did -> departmentRepository.findById(did)
                        .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + did)))
                .collect(Collectors.toSet());
        doc.setDepartments(depts);

        Doctor saved = doctorRepository.save(doc);
        return modelMapper.map(saved, DoctorDTO.class);
    }

    public void softDelete(Long id) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doc.setStatus("DELETE");
        doctorRepository.save(doc);
    }
}
