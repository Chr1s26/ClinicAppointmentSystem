package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.*;
import com.clinic.appointment.exception.*;
import com.clinic.appointment.model.*;
import com.clinic.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // ---------------- CREATE ----------------
    @Transactional
    public AppUserDTO create(AppUserCreateDTO dto, AppUser actor) {

        // duplicate username
        appUserRepository.findByUsernameIgnoreCase(dto.getUsername())
                .ifPresent(u -> { throw new DuplicateException("Username already exists"); });

        // duplicate email
        appUserRepository.findByEmailIgnoreCase(dto.getEmail())
                .ifPresent(u -> { throw new DuplicateException("Email already exists"); });

        Set<Role> roles = roleRepository.findAllById(dto.getRoleIds())
                .stream().collect(Collectors.toSet());

        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(roles)
                .createdBy(actor)
                .updatedBy(actor)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .status("ACTIVE")
                .build();

        AppUser saved = appUserRepository.save(user);
        return toDTO(saved);
    }

    // ---------------- UPDATE ----------------
    @Transactional
    public AppUserDTO update(Long id, AppUserUpdateDTO dto, AppUser actor) {

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // duplicate username
        appUserRepository.findByUsernameIgnoreCaseAndIdNot(dto.getUsername(), id)
                .ifPresent(u -> { throw new DuplicateException("Username already in use"); });

        // duplicate email
        appUserRepository.findByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)
                .ifPresent(u -> { throw new DuplicateException("Email already in use"); });

        Set<Role> roles = roleRepository.findAllById(dto.getRoleIds())
                .stream().collect(Collectors.toSet());

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRoles(roles);
        user.setUpdatedBy(actor);
        user.setUpdatedAt(LocalDate.now());

        // password change
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        AppUser saved = appUserRepository.save(user);
        return toDTO(saved);
    }

    // ---------------- DELETE (Soft Delete) ----------------
    @Transactional
    public void delete(Long id, AppUser actor) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setStatus("DELETE");
        user.setUpdatedBy(actor);
        user.setUpdatedAt(LocalDate.now());
        appUserRepository.save(user);
    }

    // ---------------- FIND ----------------
    public AppUserDTO findById(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toDTO(user);
    }

    // ---------------- MAPPER ----------------
    private AppUserDTO toDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAccountConfirmed(user.isAccountConfirmed());
        dto.setConfirmedAt(user.getConfirmedAt());
        dto.setStatus(user.getStatus());

        dto.setRoles(
                user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet())
        );

        return dto;
    }
}
