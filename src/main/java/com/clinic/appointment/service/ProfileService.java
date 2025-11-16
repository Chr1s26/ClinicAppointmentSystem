package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.AppUserDTO;
import com.clinic.appointment.dto.profile.InfoDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PatientRepository patientRepository;
    private final AuthService authService;
    private final FileService fileService;

    public void addInfo(InfoDTO dto) {
        patientRepository.findByPhoneIgnoreCase(dto.getPhone())
                .ifPresent(p -> {throw new DuplicateException("patient", dto, "phone", "patients/create", "A patient with this phone already exists");});

        Patient patient = createDTOtoEntity(dto);
        Patient saved = patientRepository.save(patient);
    }

    private Patient createDTOtoEntity(InfoDTO dto) {
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

    public AppUserDTO getUserInfo() {

        AppUser appUser = authService.getCurrentUser();

        String url = fileService.getFileName(FileType.APP_USER, appUser.getId());

        AppUserDTO dto = new AppUserDTO();
        dto.setId(appUser.getId());
        dto.setUsername(appUser.getUsername());
        dto.setEmail(appUser.getEmail());
        dto.setConfirmedAt(appUser.getConfirmedAt());
        dto.setRoles(appUser.getRoles());
        dto.setProfileUrl(url);
        dto.setCreatedAt(appUser.getCreatedAt());
        dto.setCreatedBy(appUser.getCreatedBy());
        dto.setUpdatedAt(appUser.getUpdatedAt());
        dto.setUpdatedBy(appUser.getUpdatedBy());
        dto.setPatient(appUser.getPatient());
        dto.setStatus(appUser.getStatus());
        return dto;
    }
}
