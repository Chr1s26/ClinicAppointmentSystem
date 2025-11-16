package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.*;
import com.clinic.appointment.dto.profile.ProfileRequest;
import com.clinic.appointment.exception.*;
import com.clinic.appointment.model.*;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final FileService fileService;
    private final RoleRepository roleRepository;

    public AppUserCreateDTO create(AppUserCreateDTO dto) {
//        if(appUserRepository.existsByUsernameIgnoreCaseAndEmail(dto.getUsername(),dto.getEmail())) throw new DuplicateException("user",dto,"name","users/create","An account with this name and email already exists");
        if (appUserRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new DuplicateException("user", dto, "username", "users/create", "Username already exists");
        }

        if (appUserRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new DuplicateException("user", dto, "email", "users/create", "Email already exists");
        }

        AppUser user = CreateDTOtoEntity(dto);
        AppUser saved = appUserRepository.save(user);
        return entityToCreateDTO(saved);
    }

    public AppUserUpdateDTO update(Long id, AppUserUpdateDTO dto) {

        // ensure DTO contains username/email (binding)
        // fetch existing user
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", dto, "id", "users/edit", "An account with this id cannot be found"));

        // if username changed, check uniqueness excluding this id
        if (dto.getUsername() != null && !dto.getUsername().equalsIgnoreCase(user.getUsername())) {
            if (appUserRepository.existsByUsernameIgnoreCaseAndIdNot(dto.getUsername(), id)) {
                throw new DuplicateException("user", dto, "username", "users/edit", "Username already exists");
            }
            user.setUsername(dto.getUsername());
        }

        // if email changed, check uniqueness excluding this id
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (appUserRepository.existsByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)) {
                throw new DuplicateException("user", dto, "email", "users/edit", "Email already exists");
            }
            user.setEmail(dto.getEmail());
        }

        // update password only if provided (or leave null to keep existing)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setUpdatedBy(authService.getCurrentUser());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(StatusType.ACTIVE);

        AppUser saved = appUserRepository.save(user);
        return entityToUpdateDTO(saved);
    }


//    public AppUserUpdateDTO update(Long id, AppUserUpdateDTO dto) {
//
//        appUserRepository.findByUsernameIgnoreCaseAndEmailAndIdNot(dto.getUsername(), dto.getEmail(),id)
//                .ifPresent(u -> { throw new DuplicateException("user",dto,"name","users/edit","An account with this name and email already exists");});
//
//        AppUser user = appUserRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("user",dto,"id","users/edit","An account with this id cannot be found"));
//
//        user.setUsername(dto.getUsername());
//        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setUpdatedBy(authService.getCurrentUser());
//        user.setUpdatedAt(LocalDateTime.now());
//        user.setStatus(StatusType.ACTIVE);
//
//        AppUser saved = appUserRepository.save(user);
//        return entityToUpdateDTO(saved);
//    }

    public void delete(Long id) {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("user",optionalUser.get(),"id","users","An account with this id cannot be found");
        }
        appUserRepository.delete(optionalUser.get());
    }

    public AppUserUpdateDTO findById(Long id) {
        Optional<AppUser> user = appUserRepository.findById(id);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("user",user,"id","users","An account with this id cannot be found");
        }
        return entityToUpdateDTO(user.get());
    }

    public AppUserDTO findUserById(Long id) {
        Optional<AppUser> user = appUserRepository.findById(id);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("user",user,"id","users","An account with this id cannot be found");
        }
        return entityToDTO(user.get());
    }

    private AppUserDTO entityToDTO(AppUser appUser) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(appUser.getId());
        dto.setUsername(appUser.getUsername());
        dto.setEmail(appUser.getEmail());
        dto.setConfirmedAt(appUser.getConfirmedAt());
        dto.setRoles(appUser.getRoles());
        dto.setStatus(appUser.getStatus());
        dto.setUpdatedBy(appUser.getUpdatedBy());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setCreatedBy(appUser.getCreatedBy());
        String url = fileService.getFileName(FileType.APP_USER, appUser.getId());
        dto.setProfileUrl(url);
        return dto;
    }

    public List<AppUserDTO> findAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return toListDTO(users);
    }

    public void uploadPicture(ProfileRequest profileRequest, Long id) {
        MultipartFile file = profileRequest.getFile();
        fileService.handleFileUpload(file,FileType.APP_USER, id,"S3");

        AppUser user = appUserRepository.findById(id).orElseThrow();
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(authService.getCurrentUser());
        appUserRepository.save(user);
    }

    private List<AppUserDTO> toListDTO(List<AppUser> users) {
        List<AppUserDTO> list = new ArrayList<>();
        for(AppUser user : users) {
            AppUserDTO dto = new AppUserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setUpdatedAt(user.getUpdatedAt());
            dto.setStatus(user.getStatus());
            dto.setUpdatedBy(user.getUpdatedBy());
            dto.setCreatedBy(user.getCreatedBy());
            dto.setConfirmedAt(user.getConfirmedAt());
            list.add(dto);
        }
        return list;
    }


    private AppUserCreateDTO entityToCreateDTO(AppUser saved) {
        AppUserCreateDTO dto = new AppUserCreateDTO();
        dto.setId(saved.getId());
        dto.setUsername(saved.getUsername());
        dto.setEmail(saved.getEmail());
        dto.setPassword(saved.getPassword());
        return dto;
    }

    private AppUser CreateDTOtoEntity(AppUserCreateDTO dto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(dto.getUsername());
        appUser.setEmail(dto.getEmail());
        appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setCreatedBy(authService.getCurrentUser());
        appUser.setStatus(StatusType.ACTIVE);
        Role role = roleRepository.findByRoleName("PATIENT").orElseThrow(() -> new ResourceNotFoundException("role",dto,"roleName","appUsers","Role Name not found."));
        appUser.setRoles(Set.of(role));

        return appUser;
    }

    private AppUserUpdateDTO entityToUpdateDTO(AppUser saved) {
        AppUserUpdateDTO dto = new AppUserUpdateDTO();
        dto.setId(saved.getId());
        dto.setUsername(saved.getUsername());
        dto.setEmail(saved.getEmail());
        dto.setPassword(saved.getPassword());
        return dto;
    }
}
