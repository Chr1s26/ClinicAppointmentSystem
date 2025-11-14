package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.AppUserDTO;
import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.dto.patient.PatientDTO;
import com.clinic.appointment.dto.patient.PatientUpdateDTO;
import com.clinic.appointment.dto.profile.ProfileRequest;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AuthService authService;
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    @Transactional
    public PatientCreateDTO create(PatientCreateDTO dto) {

        patientRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(p -> {
                    throw new DuplicateException(
                            "patient", dto, "phone", "patients/create",
                            "A patient with this phone already exists");
                });

        patientRepository.findByEmailIgnoreCase(dto.getEmail())
                .ifPresent(p -> {
                    throw new DuplicateException(
                            "patient", dto, "email", "patients/create",
                            "A patient with this email already exists");
                });

        Patient patient = createDTOtoEntity(dto);
        Patient saved = patientRepository.save(patient);

        return entityToCreateDTO(saved);
    }

    @Transactional
    public PatientUpdateDTO update(Long id, PatientUpdateDTO dto) {

        patientRepository.findByPhoneIgnoreCaseAndIdNot(dto.getPhone(), id)
                .ifPresent(p -> {
                    throw new DuplicateException(
                            "patient", dto, "phone", "patients/edit",
                            "This phone number is already used by another patient");
                });

        patientRepository.findByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)
                .ifPresent(p -> {
                    throw new DuplicateException(
                            "patient", dto, "email", "patients/edit",
                            "This email is already used by another patient");
                });

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("patient", dto, "id", "patients/edit",
                                "A patient with this ID cannot be found")
                );

        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGenderType(dto.getGenderType());
        patient.setPatientType(dto.getPatientType());

        patient.setUpdatedAt(LocalDateTime.now());
        patient.setUpdatedBy(authService.getCurrentUser());
        patient.setStatus(StatusType.ACTIVE);

        Patient saved = patientRepository.save(patient);
        return entityToUpdateDTO(saved);
    }

    public void delete(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if(optionalPatient.isEmpty()) {
            throw new ResourceNotFoundException("patient",optionalPatient.get(),"id","patients","An account with this id cannot be found");
        }
        patientRepository.delete(optionalPatient.get());
    }

//    @Transactional
//    public void delete(Long id) {
//        Optional<Patient> optional = patientRepository.findById(id);
//        if (optional.isEmpty()) {
//            throw new ResourceNotFoundException(
//                    "patient", optional, "id", "patients",
//                    "A patient with this ID cannot be found"
//            );
//        }
//
//        Patient p = optional.get();
//        p.setStatus(StatusType.DELETED);
//
//        patientRepository.save(p);
//    }

    public PatientUpdateDTO findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()) {
            throw new ResourceNotFoundException("patient",patient,"id","patients","An account with this id cannot be found");
        }
        return entityToUpdateDTO(patient.get());
    }

    public PatientDTO findPatientById(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "patient", null, "id", "patients/view",
                        "A patient with this ID cannot be found")
                );
        return entityToDTO(p);
    }

    public List<PatientDTO> findAllPatients() {
        List<Patient> list = patientRepository.findAll();
        return toListDTO(list);
    }

    private Patient createDTOtoEntity(PatientCreateDTO dto) {
        Patient p = new Patient();
        p.setName(dto.getName());
        p.setPhone(dto.getPhone());
        p.setEmail(dto.getEmail());
        p.setAddress(dto.getAddress());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setGenderType(dto.getGenderType());
        p.setPatientType(dto.getPatientType());

        p.setStatus(StatusType.ACTIVE);
        p.setCreatedAt(LocalDateTime.now());
        p.setCreatedBy(authService.getCurrentUser());

        AppUser appUser = appUserRepository.findById(dto.getAppUserId()).orElseThrow(() -> new ResourceNotFoundException("patient", dto, "id", "patients", "An account with this id cannot be found"));
        p.setAppUser(appUser);

        return p;
    }

    private PatientDTO entityToDTO(Patient p) {
        PatientDTO dto = new PatientDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPhone(p.getPhone());
        dto.setEmail(p.getEmail());
        dto.setAddress(p.getAddress());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setGenderType(p.getGenderType());
        dto.setPatientType(p.getPatientType());
        dto.setStatus(p.getStatus());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        dto.setCreatedBy(p.getCreatedBy());
        dto.setUpdatedBy(p.getUpdatedBy());
        return dto;
    }

    private PatientCreateDTO entityToCreateDTO(Patient saved) {
        PatientCreateDTO dto = new PatientCreateDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setEmail(saved.getEmail());
        dto.setPhone(saved.getPhone());
        return dto;
    }

    private PatientUpdateDTO entityToUpdateDTO(Patient p) {
        PatientUpdateDTO dto = new PatientUpdateDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setEmail(p.getEmail());
        dto.setPhone(p.getPhone());
        dto.setAddress(p.getAddress());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setGenderType(p.getGenderType());
        dto.setPatientType(p.getPatientType());
        return dto;
    }

    private List<PatientDTO> toListDTO(List<Patient> patients) {
        List<PatientDTO> list = new ArrayList<>();
        for (Patient p : patients) {
            list.add(entityToDTO(p));
        }
        return list;
    }
}
