package com.clinic.appointment.service;

import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.PatientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PatientRepository patientRepository;
    private final AuthService authService;
    private final AppUserRepository appUserRepository;

    public void addInfo(PatientCreateDTO dto) {
        patientRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(p -> {throw new DuplicateException("patient", dto, "phone", "patients/create", "A patient with this phone already exists");});

        Patient patient = createDTOtoEntity(dto);
        Patient saved = patientRepository.save(patient);
    }

    private Patient createDTOtoEntity(PatientCreateDTO dto) {
        Patient p = new Patient();
        p.setName(dto.getName());
        p.setPhone(dto.getPhone());
        p.setAddress(dto.getAddress());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setGenderType(dto.getGenderType());
        p.setPatientType(dto.getPatientType());
        p.setStatus(StatusType.ACTIVE);
        p.setCreatedAt(LocalDateTime.now());
        p.setCreatedBy(authService.getCurrentUser());
        AppUser appUser = authService.getCurrentUser();
        if(appUser != null) {
            p.setAppUser(appUser);
        }
        return p;
    }
}
