package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctor.DoctorCreateDTO;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    private final AuthService authService;

    public DoctorDTO findById(Long id) {
        Optional<Doctor> d = doctorRepository.findById(id);
        if(d.isEmpty()) {
            throw new ResourceNotFoundException("doctor",d,"id","doctors","A doctor with this id not found");
        }
        return modelMapper.map(d, DoctorDTO.class);
    }

    public DoctorDTO create(DoctorCreateDTO dto) {
        doctorRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(d -> { throw new DuplicateException("doctor",dto,"phone","doctors/create","A doctor with the same phone number already exists"); });

        AppUser appUser = appUserRepository.findById(dto.getAppUserId()).orElseThrow(() ->  new ResourceNotFoundException("doctor",dto,"appUserId","doctors","An account with this id not found"));
        AppUser currentUser = authService.getCurrentUser();

        Doctor doc = new Doctor();
        doc.setName(dto.getName());
        doc.setPhone(dto.getPhone());
        doc.setAddress(dto.getAddress());
        doc.setDateOfBirth(dto.getDateOfBirth());
        doc.setGenderType(dto.getGenderType());
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setCreatedBy(currentUser);
        doc.setAppUser(appUser);
        doc.setStatus(StatusType.ACTIVE);

        Set<Department> depts = dto.getDepartmentIds().stream()
                .map(id -> departmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("department",dto,"id","departments","A department with this id not found")))
                .collect(Collectors.toSet());
        doc.setDepartments(depts);

        Doctor saved = doctorRepository.save(doc);
        return modelMapper.map(saved, DoctorDTO.class);
    }

    public DoctorDTO update(Long id, DoctorUpdateDTO dto) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("doctor",dto,"id","doctors","A doctor with this id not found"));

        doctorRepository.findByPhoneIgnoreCaseAndIdNot(dto.getPhone(), id)
                .ifPresent(d -> { throw new DuplicateException("doctor",dto,"phone","doctors/update","A doctor with the same phone number already exists"); });

        AppUser currentUser = authService.getCurrentUser();
        doc.setName(dto.getName());
        doc.setPhone(dto.getPhone());
        doc.setAddress(dto.getAddress());
        doc.setDateOfBirth(dto.getDateOfBirth());
        doc.setGenderType(dto.getGenderType());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setUpdatedBy(currentUser);

        Set<Department> depts = dto.getDepartmentIds().stream()
                .map(did -> departmentRepository.findById(did)
                        .orElseThrow(() -> new ResourceNotFoundException("department",dto,"id","departments","A department with this id not found")))
                .collect(Collectors.toSet());
        doc.setDepartments(depts);

        Doctor saved = doctorRepository.save(doc);
        return modelMapper.map(saved, DoctorDTO.class);
    }

    public void softDelete(Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if(doctorOptional.isEmpty()) {
            throw new ResourceNotFoundException("doctor",doctorOptional,"id","doctors","A doctor with this id not found");
        }
        Doctor doc = doctorOptional.get();
        doc.setStatus(StatusType.DELETED);
        doctorRepository.save(doc);
    }
}
