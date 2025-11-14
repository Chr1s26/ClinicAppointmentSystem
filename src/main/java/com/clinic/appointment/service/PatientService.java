package com.clinic.appointment.service;

import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.dto.patient.PatientDTO;
import com.clinic.appointment.dto.patient.PatientUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.NotFoundException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public PatientDTO findById(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return modelMapper.map(p, PatientDTO.class);
    }

    @Transactional
    public PatientDTO create(PatientCreateDTO dto, AppUser user) {

        patientRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(p -> { throw new DuplicateException("Phone already used by another patient"); });

        patientRepository.findByEmailIgnoreCase(dto.getEmail())
                .ifPresent(p -> { throw new DuplicateException("Email already used by another patient"); });

        Patient p = new Patient();
        p.setName(dto.getName());
        p.setPhone(dto.getPhone());
        p.setEmail(dto.getEmail());
        p.setAddress(dto.getAddress());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setGenderType(dto.getGenderType());
        p.setPatientType(dto.getPatientType());
        p.setCreatedAt(LocalDate.now());
        p.setUpdatedAt(LocalDate.now());
        p.setCreatedBy(user);
        p.setUpdatedBy(user);
        p.setStatus("ACTIVE");

        AppUser au = new AppUser();
        au.setId(dto.getAppUserId());
        p.setAppUser(au);

        Patient saved = patientRepository.save(p);
        return modelMapper.map(saved, PatientDTO.class);
    }

    @Transactional
    public PatientDTO update(Long id, PatientUpdateDTO dto, AppUser user) {

        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patientRepository.findByPhoneIgnoreCaseAndIdNot(dto.getPhone(), id)
                .ifPresent(x -> { throw new DuplicateException("Phone already used by another patient"); });

        patientRepository.findByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)
                .ifPresent(x -> { throw new DuplicateException("Email already used by another patient"); });

        p.setName(dto.getName());
        p.setPhone(dto.getPhone());
        p.setEmail(dto.getEmail());
        p.setAddress(dto.getAddress());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setGenderType(dto.getGenderType());
        p.setPatientType(dto.getPatientType());
        p.setUpdatedAt(LocalDate.now());
        p.setUpdatedBy(user);

        Patient saved = patientRepository.save(p);
        return modelMapper.map(saved, PatientDTO.class);
    }

    public void softDelete(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        p.setStatus("DELETE");
        patientRepository.save(p);
    }
}
