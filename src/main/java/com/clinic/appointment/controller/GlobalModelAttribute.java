package com.clinic.appointment.controller;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final AuthService authService;
    private final FileService fileService;

    @ModelAttribute("navbarProfileUrl")
    public String addProfileUrlToSideBar() {
        try {
            AppUser user = authService.getCurrentUser();
            if (user == null) return "/images/default-profile.png";

            String url = fileService.getFileName(FileType.APP_USER, user.getId());
            return (url != null && !url.isBlank()) ? url : "/images/default-profile.png";
        } catch (Exception e) {
            return "/images/default-profile.png";
        }
    }

    @ModelAttribute("navbarProfileName")
    public String addProfileNameToSideBar() {
        try {
            AppUser user = authService.getCurrentUser();
            if (user == null) return "User";

            String name = user.getUsername();
            return (name != null && !name.isBlank()) ? name : "User";
        } catch (Exception e) {
            return "User";
        }
    }

    @ModelAttribute("needsPatientInfo")
    public Boolean showPatientInfoWarning() {
        try {
            return authService.needsPatientInfo();
        } catch (Exception e) {
            return false;
        }
    }

    @ModelAttribute("navbarDoctorId")
    public Long addDoctorIdToSidebar() {
        try {
            AppUser user = authService.getCurrentUser();
            if (user == null) return null;

            if (user.getDoctor() != null) {
                return user.getDoctor().getId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @ModelAttribute("user")
    public AppUser addUserToModel() {
        try {
            return authService.getCurrentUser();
        } catch (Exception e) {
            return null;
        }
    }


}
