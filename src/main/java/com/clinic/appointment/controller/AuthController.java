package com.clinic.appointment.controller;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String showLoginForm(@ModelAttribute AppUser appUser, Model model) {
        try{
            userService.registerNewUser(appUser);
        }catch(Exception e){
            model.addAttribute("registrationError",e.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

}
