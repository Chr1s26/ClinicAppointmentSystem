//package com.clinic.appointment.api.v1.controller;
//
//import com.clinic.appointment.dto.api.appUser.AppUserDTO;
//import com.clinic.appointment.model.AppUser;
//import com.clinic.appointment.model.Role;
//import com.clinic.appointment.repository.AppUserRepository;
//import com.clinic.appointment.repository.RoleRepository;
//import com.clinic.appointment.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api/v1/app-users")
//public class AppUserRestController {
//
//    @Autowired
//    private AppUserRepository appUserRepository;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping
//    public ResponseEntity<?> addAppUser(@RequestBody AppUser appUser) {
//        AppUser currentUser = authService.getCurrentUser();
//        System.out.println("*****************************");
//        System.out.println(currentUser.getId());
//        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
//        appUser.setCreatedBy(currentUser);
//        appUser.setCreatedAt(LocalDate.now());
//        Set<Role> persistentRoles = new HashSet<>();
//        for(Role role : appUser.getRoles()){
//            Role exRole = roleRepository.findById(role.getId()).orElse(null);
//            if(exRole != null){persistentRoles.add(exRole);}
//        }
//        appUser.setRoles(persistentRoles);
//        appUser = appUserRepository.save(appUser);
//
//        AppUserDTO appUserDTO = new AppUserDTO();
//        appUserDTO.setId(appUser.getId());
//        appUserDTO.setUsername(appUser.getUsername());
//        appUserDTO.setEmail(appUser.getEmail());
//        appUserDTO.setRoles(new ArrayList<>(appUser.getRoles()));
//        appUserDTO.setIsAccountConfirmed(appUser.isAccountConfirmed());
//        appUserDTO.setCreatedAt(appUser.getCreatedAt());
//        appUserDTO.setCreatedBy(appUser.getCreatedBy());
//        appUserDTO.setUpdatedBy(appUser.getUpdatedBy());
//        return new ResponseEntity<>(appUserDTO,HttpStatus.CREATED);
//    }
//}
