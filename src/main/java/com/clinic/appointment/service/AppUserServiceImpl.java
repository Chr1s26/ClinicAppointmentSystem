package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.AppUserCreateDTO;
import com.clinic.appointment.exception.AccountNotConfirmedException;
import com.clinic.appointment.exception.RoleNotFoundException;
import com.clinic.appointment.exception.UserNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Role;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements UserService{

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String parameter) throws UsernameNotFoundException {

        AppUser appUser = appUserRepository.findByEmail(parameter)
                .orElseGet(() -> appUserRepository.findByUsernameIgnoreCase(parameter)
                        .orElseThrow(() -> new UserNotFoundException("User not found with username or email", "/login")));

        if(!appUser.isAccountConfirmed()){
            throw new AccountNotConfirmedException("AccountNotConfirmedException");
        }
        return UserDetailsImpl.build(appUser);
    }

    @Override
    public AppUser registerNewUser(AppUserCreateDTO appUserCreateDTO) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserCreateDTO.getUsername());
        appUser.setEmail(appUserCreateDTO.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        Role userRole = roleRepository.findByRoleName("PATIENT").orElseThrow(() -> new RoleNotFoundException("Invalid role"));
        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setStatus(StatusType.ACTIVE.name());
        appUser.setRoles(Collections.singleton(userRole));
        return appUserRepository.save(appUser);
    }
}
