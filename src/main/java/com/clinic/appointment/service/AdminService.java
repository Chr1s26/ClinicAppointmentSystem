package com.clinic.appointment.service;

import com.clinic.appointment.dto.admin.AdminCreateDTO;
import com.clinic.appointment.dto.admin.AdminDTO;
import com.clinic.appointment.dto.admin.AdminUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AdminRepository;
import com.clinic.appointment.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthService authService;
    private final AppUserRepository appUserRepository;

    @Transactional
    public AdminCreateDTO create(AdminCreateDTO dto) {

        adminRepository.findByPhone(dto.getPhone())
                .ifPresent(a -> {
                    throw new DuplicateException(
                            "admin", dto, "phone", "admins/create",
                            "An admin with this phone already exists");
                });

        adminRepository.findByEmailIgnoreCase(dto.getEmail())
                .ifPresent(a -> {
                    throw new DuplicateException(
                            "admin", dto, "email", "admins/create",
                            "An admin with this email already exists");
                });

        Admin admin = createDTOtoEntity(dto);
        Admin saved = adminRepository.save(admin);

        return entityToCreateDTO(saved);
    }

    @Transactional
    public AdminUpdateDTO update(Long id, AdminUpdateDTO dto) {

        adminRepository.findByPhoneAndIdNot(dto.getPhone(), id)
                .ifPresent(a -> {
                    throw new DuplicateException(
                            "admin", dto, "phone", "admins/edit",
                            "This phone number is already used by another admin");
                });

        adminRepository.findByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)
                .ifPresent(a -> {
                    throw new DuplicateException(
                            "admin", dto, "email", "admins/edit",
                            "This email is already used by another admin");
                });

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("admin", dto, "id", "admins/edit",
                                "An admin with this ID cannot be found")
                );

        admin.setName(dto.getName());
        admin.setPhone(dto.getPhone());
        admin.setEmail(dto.getEmail());
        admin.setAddress(dto.getAddress());
        admin.setDateOfBirth(dto.getDateOfBirth());
        admin.setGenderType(dto.getGenderType());

        admin.setUpdatedAt(LocalDateTime.now());
        admin.setUpdatedBy(authService.getCurrentUser());
        admin.setStatus(StatusType.ACTIVE);

        Admin saved = adminRepository.save(admin);
        return entityToUpdateDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);

        if (optionalAdmin.isEmpty()) {
            throw new ResourceNotFoundException(
                    "admin", optionalAdmin, "id", "admins",
                    "An admin with this id cannot be found");
        }

        adminRepository.delete(optionalAdmin.get());
    }

    public AdminUpdateDTO findById(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isEmpty()) {
            throw new ResourceNotFoundException(
                    "admin", admin, "id", "admins",
                    "An admin with this id cannot be found");
        }
        return entityToUpdateDTO(admin.get());
    }

    public AdminDTO findAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "admin", null, "id", "admins/view",
                        "An admin with this ID cannot be found")
                );
        return entityToDTO(admin);
    }

    public List<AdminDTO> findAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return toListDTO(admins);
    }


    private Admin createDTOtoEntity(AdminCreateDTO dto) {
        Admin admin = new Admin();
        admin.setName(dto.getName());
        admin.setPhone(dto.getPhone());
        admin.setEmail(dto.getEmail());
        admin.setAddress(dto.getAddress());
        admin.setDateOfBirth(dto.getDateOfBirth());
        admin.setGenderType(dto.getGenderType());

        admin.setStatus(StatusType.ACTIVE);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setCreatedBy(authService.getCurrentUser());

        if (dto.getAppUserId() != null) {
            AppUser user = appUserRepository.findById(dto.getAppUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "admin", dto, "id", "admins",
                            "An account with this id cannot be found"));
            admin.setAppUser(user);
        }

        return admin;
    }

    private AdminDTO entityToDTO(Admin a) {
        AdminDTO dto = new AdminDTO();
        dto.setId(a.getId());
        dto.setName(a.getName());
        dto.setPhone(a.getPhone());
        dto.setEmail(a.getEmail());
        dto.setAddress(a.getAddress());
        dto.setGenderType(a.getGenderType());
        dto.setDateOfBirth(a.getDateOfBirth());

        dto.setStatus(a.getStatus());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        dto.setCreatedBy(a.getCreatedBy());
        dto.setUpdatedBy(a.getUpdatedBy());
        return dto;
    }

    private AdminCreateDTO entityToCreateDTO(Admin saved) {
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setEmail(saved.getEmail());
        dto.setPhone(saved.getPhone());
        dto.setAddress(saved.getAddress());
        dto.setGenderType(saved.getGenderType());
        dto.setDateOfBirth(saved.getDateOfBirth());
        return dto;
    }

    private AdminUpdateDTO entityToUpdateDTO(Admin a) {
        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setId(a.getId());
        dto.setName(a.getName());
        dto.setEmail(a.getEmail());
        dto.setPhone(a.getPhone());
        dto.setAddress(a.getAddress());
        dto.setGenderType(a.getGenderType());
        dto.setDateOfBirth(a.getDateOfBirth());
        return dto;
    }

    private List<AdminDTO> toListDTO(List<Admin> admins) {
        List<AdminDTO> list = new ArrayList<>();
        for (Admin a : admins) {
            list.add(entityToDTO(a));
        }
        return list;
    }
}
