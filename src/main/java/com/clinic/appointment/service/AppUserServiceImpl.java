package com.clinic.appointment.service;

import com.clinic.appointment.exception.AccountNotConfirmedException;
import com.clinic.appointment.exception.RoleNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Role;
import com.clinic.appointment.model.constant.Status;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!appUser.isAccountConfirmed()){
            throw new AccountNotConfirmedException("AccountNotConfirmedException");
        }
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(),appUser.getPassword(),getGrantedAuthorities(appUser.getRoles()));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" +role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public AppUser registerNewUser(AppUser appUser) {

        if(appUserRepository.existsByEmail(appUser.getEmail())){
            throw new RuntimeException("Email already exists!!");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow(() -> new RoleNotFoundException("Invalid role"));
        appUser.setCreatedAt(LocalDate.now());
        appUser.setStatus(Status.ACTIVE.name());
        appUser.setRoles(Collections.singleton(userRole));
        return appUserRepository.save(appUser);
    }
}
