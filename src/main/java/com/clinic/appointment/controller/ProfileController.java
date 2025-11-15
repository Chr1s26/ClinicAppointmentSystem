package com.clinic.appointment.controller;

import com.clinic.appointment.dto.appUser.AppUserDTO;
import com.clinic.appointment.dto.profile.InfoDTO;
import com.clinic.appointment.dto.profile.ProfileRequest;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.service.AppUserService;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final AuthService authService;
    private final ProfileService profileService;
    private final AppUserService userService;

    @GetMapping
    public String viewProfile(Model model) {
        AppUserDTO appUser = profileService.getUserInfo();

        model.addAttribute("user", appUser);
        model.addAttribute("request",new ProfileRequest());
        boolean hasPatient = (appUser.getPatient() != null);
        model.addAttribute("hasPatient", hasPatient);

        return "profile/view";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("patient", new InfoDTO());
        model.addAttribute("genderType", GenderType.values());
        model.addAttribute("patientType", PatientType.values());
        return "profile/create";
    }

    @PostMapping("/create")
    public String addInfo(@Valid @ModelAttribute("patient") InfoDTO dto,
                                BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "profile/create";
        }
        profileService.addInfo(dto);
        return "redirect:/profile";
    }

    @PostMapping("/{id}/updatePicture")
    public String updateProfilePicture(@ModelAttribute("request") ProfileRequest profileRequest,@PathVariable("id") Long userId) {
        userService.uploadPicture(profileRequest,userId);
        return "redirect:/profile";
    }

}
