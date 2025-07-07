package com.clinic.appointment.service;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Role;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(),appUser.getPassword(),getGrantedAuthorities(appUser.getRoles()));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }


    @Override
    public AppUser registerNewUser(AppUser appUser) {
        if(appUserRepository.existsByUsername(appUser.getUsername())){
            throw new RuntimeException("Username already exists!!");
        }
        if(appUserRepository.existsByEmail(appUser.getEmail())){
            throw new RuntimeException("Email already exists!!");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        Role userRole = roleRepository.findByRoleName("USER").orElseGet(() -> {
            Role role = new Role();
            role.setRoleName("USER");
            return roleRepository.save(role);
        });
        appUser.setRoles(Collections.singleton(userRole));
        return null;
    }
}
