package com.clinic.appointment.controller;

import com.clinic.appointment.dto.appUser.AppUserCreateDTO;
import com.clinic.appointment.dto.forget_password.EmailForm;
import com.clinic.appointment.dto.forget_password.ResetPasswordDTO;
import com.clinic.appointment.exception.OtpExpiredException;
import com.clinic.appointment.exception.OtpInvalidException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.OtpService;
import com.clinic.appointment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class AuthController {
    private final UserService userService;
    private final AppUserRepository appUserRepository;
    private final OtpService otpService;
    private final AuthService authService;

    @GetMapping
    public String showRegistrationForm(@RequestParam(value = "registration", required = false, defaultValue = "false") String registration, Model model) {
        model.addAttribute("appUserCreateDTO", new AppUserCreateDTO());
        if(registration.equalsIgnoreCase("true")) {
            model.addAttribute("registrationError", "Registration Error");
        }
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("appUserCreateDTO") AppUserCreateDTO appUserCreateDTO, BindingResult br, Model model) {

        if (appUserCreateDTO == null) {
            model.addAttribute("registrationError", "Form binding failed. DTO is null.");
            return "register";
        }

        if (appUserRepository.existsByUsernameIgnoreCase(appUserCreateDTO.getUsername())) br.rejectValue("username", "duplicate", "This username is already taken");
        if (appUserRepository.existsByEmail(appUserCreateDTO.getEmail())) br.rejectValue("email", "duplicate", "An account with this email already exists");
        if (br.hasErrors()) return "register";
        try{
            userService.registerNewUser(appUserCreateDTO);
        }catch(Exception e){
            model.addAttribute("registrationError","Registration failed: " + e.getMessage());
            return "register";
        }
        return "redirect:/login?registered=true";
    }


    @GetMapping("/forget-password")
    public String showForgetPasswordForm(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String checkEmailProcess(@Valid @ModelAttribute("emailForm") EmailForm emailForm, HttpSession session, BindingResult bindingResult) {
        Optional<AppUser> userOp = appUserRepository.findByEmail(emailForm.getEmail());
        if (userOp.isEmpty()) bindingResult.rejectValue("email", "not found", "An account with this email doesn't exist");
        if (bindingResult.hasErrors()) return "forget-password";

        otpService.sendOtp(userOp.get());
        session.setAttribute("resetEmail", emailForm.getEmail());
        return "redirect:/confirm-otp";
    }

    @GetMapping("/confirm-otp")
    public String showConfirmOtpForm() {
        return "confirm-otp";
    }

    @PostMapping("/confirm-otp")
    public String processOtp(@RequestParam("otp") String otp, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("otpError", "Session expired. Please try again.");
            return "forget-password";
        }

        try{
            otpService.isOtpValid(email, otp);
        }catch (OtpExpiredException e){
            model.addAttribute("otpError", "OTP is expired. Please try again.");
            return "confirm-otp";
        }catch (OtpInvalidException e){
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "confirm-otp";
        }

        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("resetPasswordForm", new ResetPasswordDTO());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordDTO form, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "forget-password";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        authService.resetPassword(email, form.getPassword());
        session.removeAttribute("resetEmail");

        return "redirect:/login";
    }

}
