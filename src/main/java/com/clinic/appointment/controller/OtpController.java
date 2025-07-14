package com.clinic.appointment.controller;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class OtpController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/confirm-account/otp")
    public String showConfirmAccountOtp(@RequestParam("email") String email,@RequestParam("message") String message ,Model model){
        model.addAttribute("email",email);
        model.addAttribute("message",message);
        return "confirm-account/otp";
    }

    @PostMapping("/confirm-account/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,@RequestParam String email){
        if(otp.equals("123")){
            AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println(appUser.getEmail());
            appUser.setConfirmedAt(LocalDate.now());
            appUserRepository.save(appUser);
            return "redirect:/login";
        }
        return "redirect:/confirm-account/otp";
    }
}
