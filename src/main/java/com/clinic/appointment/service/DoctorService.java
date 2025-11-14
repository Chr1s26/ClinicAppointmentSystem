package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctor.DoctorCreateDTO;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public DoctorCreateDTO create(DoctorCreateDTO dto) {
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
        doc.setEmail(dto.getEmail());
        doc.setStatus(StatusType.ACTIVE);

        Doctor saved = doctorRepository.save(doc);
        return entityToCreateDTO(saved);
    }

    public DoctorUpdateDTO update(Long id, DoctorUpdateDTO dto) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("doctor",dto,"id","doctors","A doctor with this id not found"));

        doctorRepository.findByPhoneIgnoreCaseAndIdNot(dto.getPhone(), id)
                .ifPresent(d -> { throw new DuplicateException("doctor",dto,"phone","doctors/update","A doctor with the same phone number already exists"); });

        AppUser currentUser = authService.getCurrentUser();
        doc.setName(dto.getName());
        doc.setPhone(dto.getPhone());
        doc.setAddress(dto.getAddress());
        doc.setEmail(dto.getEmail());
        doc.setDateOfBirth(dto.getDateOfBirth());
        doc.setGenderType(dto.getGenderType());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setUpdatedBy(currentUser);

        Doctor saved = doctorRepository.save(doc);
        return entityToUpdateDTO(saved);
    }

    public void softDelete(Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if(doctorOptional.isEmpty()) {
            throw new ResourceNotFoundException("doctor",doctorOptional,"id","doctors","A doctor with this id not found");
        }
        doctorRepository.deleteById(id);
    }

    public List<DoctorDTO> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return entityToDTOList(doctors);
    }

    private List<DoctorDTO> entityToDTOList(List<Doctor> doctors) {
        List<DoctorDTO> dtos = new ArrayList<>();
        for(Doctor doc : doctors) {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getName());
            dto.setPhone(doc.getPhone());
            dto.setAddress(doc.getAddress());
            dto.setDateOfBirth(doc.getDateOfBirth());
            dto.setGenderType(doc.getGenderType());
            dto.setEmail(doc.getEmail());
            dto.setStatus(doc.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    private DoctorCreateDTO entityToCreateDTO(Doctor saved) {
        DoctorCreateDTO dto = new DoctorCreateDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setPhone(saved.getPhone());
        dto.setAddress(saved.getAddress());
        dto.setDateOfBirth(saved.getDateOfBirth());
        dto.setGenderType(saved.getGenderType());
        dto.setEmail(saved.getEmail());
        return dto;
    }

    private DoctorUpdateDTO entityToUpdateDTO(Doctor saved) {
        DoctorUpdateDTO dto = new DoctorUpdateDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setPhone(saved.getPhone());
        dto.setAddress(saved.getAddress());
        dto.setDateOfBirth(saved.getDateOfBirth());
        dto.setGenderType(saved.getGenderType());
        dto.setEmail(saved.getEmail());
        return dto;
    }
}
