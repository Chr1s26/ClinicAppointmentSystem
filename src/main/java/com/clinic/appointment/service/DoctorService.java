package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctor.DoctorCreateDTO;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Role;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    public DoctorDTO findById(Long id) {
        Optional<Doctor> d = doctorRepository.findById(id);
        if(d.isEmpty()) {
            throw new ResourceNotFoundException("doctor",d,"id","doctors","A doctor with this id not found");
        }
        return entitytoDTO(d.get());
    }

    public DoctorUpdateDTO findDoctorById(Long id) {
        Optional<Doctor> d = doctorRepository.findById(id);
        if(d.isEmpty()) {
            throw new ResourceNotFoundException("doctor",d,"id","doctors","A doctor with this id not found");
        }
        return entityToUpdateDTO(d.get());
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
        doc.setCreatedBy(currentUser);
        doc.setAppUser(appUser);
        Set<Department> depts = departmentRepository.findAllById(dto.getDepartmentIds()).stream().collect(Collectors.toSet());
        doc.setDepartments(depts);
        doc.setStatus(StatusType.ACTIVE);
        Role role = roleRepository.findByRoleName("DOCTOR").orElseThrow(() -> new ResourceNotFoundException("doctor",dto,"name","doctors/create","A doctor with the same phone number already exists"));
        if(appUser.getRoles() == null){
            appUser.setRoles(new HashSet<>());
        }
        appUser.getRoles().add(role);
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
        doc.setDateOfBirth(dto.getDateOfBirth());
        Set<Department> depts = departmentRepository.findAllById(dto.getDepartmentIds()).stream().collect(Collectors.toSet());
        doc.setDepartments(depts);
        doc.setGenderType(dto.getGenderType());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setUpdatedBy(currentUser);

        Doctor saved = doctorRepository.save(doc);
        return entityToUpdateDTO(saved);
    }

    public void softDelete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("doctor",id,"id","doctors","A doctor with this id not found"));

        doctor.getDepartments().forEach(dept -> dept.getDoctors().remove(doctor));
        doctor.getDepartments().clear();

        AppUser appUser = doctor.getAppUser();
        appUser.setDoctor(null);
        appUser.getRoles().removeIf(role -> role.getRoleName().equalsIgnoreCase("DOCTOR"));

        doctor.setAppUser(null);

        doctorRepository.delete(doctor);
        appUserRepository.save(appUser);
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
            dto.setStatus(doc.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    private DoctorDTO entitytoDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setPhone(doctor.getPhone());
        dto.setAddress(doctor.getAddress());
        dto.setDateOfBirth(doctor.getDateOfBirth());
        dto.setGenderType(doctor.getGenderType());
        dto.setStatus(doctor.getStatus());
        dto.setDepartments(doctor.getDepartments());
        dto.setAppUser(doctor.getAppUser());
        return dto;
    }

    private DoctorCreateDTO entityToCreateDTO(Doctor saved) {
        DoctorCreateDTO dto = new DoctorCreateDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setPhone(saved.getPhone());
        dto.setAddress(saved.getAddress());
        dto.setDateOfBirth(saved.getDateOfBirth());
        dto.setGenderType(saved.getGenderType());
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
        dto.setDepartmentIds(
                saved.getDepartments().stream()
                        .map(Department::getId)
                        .collect(Collectors.toSet())
        );
        return dto;
    }


}
