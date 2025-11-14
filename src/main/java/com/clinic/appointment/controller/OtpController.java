package com.clinic.appointment.controller;

import com.clinic.appointment.exception.OtpExpiredException;
import com.clinic.appointment.exception.OtpInvalidException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.service.OtpService;
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
    @Autowired
    private OtpService otpService;

    @GetMapping("/confirm-account/otp")
    public String showConfirmAccountOtp(@RequestParam("email") String email, @RequestParam("message") String message , Model model){
        model.addAttribute("email",email);
        model.addAttribute("message",message);
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!message.equals("OTP is Invalid")){
            otpService.sendOtp(appUser);
        }
        return "confirm-account/otp";
    }

    @PostMapping("/confirm-account/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,@RequestParam String email){
        try{
            otpService.isOtpValid(email, otp);
            return "redirect:/login?otpMessage=true";
        }catch (OtpExpiredException e){
            String encodedMessage = "OTP is expired";
            return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
        }catch (OtpInvalidException e){
            String encodedMessage = "OTP is Invalid";
            return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
        }
    }

    @PostMapping("/confirm-account/resend-otp")
    public String resendOtp(@RequestParam String email){
        String encodedMessage = "OTP is resent";
        return "redirect:/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage;
    }
}
