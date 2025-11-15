package com.clinic.appointment.controller;

import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
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

    @GetMapping
    public String viewProfile(Model model) {
        AppUser user = authService.getCurrentUser();

        model.addAttribute("user", user);

        boolean hasPatient = (user.getPatient() != null);
        model.addAttribute("hasPatient", hasPatient);

        return "profile/view";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("patient", new PatientCreateDTO());
        model.addAttribute("genderType", GenderType.values());
        model.addAttribute("patientType", PatientType.values());
        return "profile/create";
    }

    @PostMapping("/create")
    public String addInfo(@Valid @ModelAttribute("patient") PatientCreateDTO dto,
                                BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "profile/create";
        }
        profileService.addInfo(dto);
        return "redirect:/profile";
    }

}
