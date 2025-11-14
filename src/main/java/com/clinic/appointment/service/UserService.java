package com.clinic.appointment.service;

import com.clinic.appointment.dto.appUser.AppUserCreateDTO;
import com.clinic.appointment.model.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser registerNewUser(AppUserCreateDTO appUserCreateDTO);
}
