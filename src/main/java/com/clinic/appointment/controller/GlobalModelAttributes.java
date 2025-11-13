package com.clinic.appointment.controller;

import com.clinic.appointment.dto.ProfileDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthService authService;

    @ModelAttribute("profile")
    public ProfileDTO addProfileToModel() {

        AppUser user = authService.getCurrentUser();

        if (user == null) {
            return null; // login page, register, forgot password, etc.
        }

        String activeRole = profileService.extractPrimaryRole(user.getRoles());

        if (activeRole == null) {
            return null;
        }

        return profileService.getProfileUrl(activeRole);
    }
}
