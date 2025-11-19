package com.clinic.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false, defaultValue = "false") String error,
                        @RequestParam(value = "registered", required = false, defaultValue = "false") String registered,
                        @RequestParam(value = "otpMessage", required = false, defaultValue = "false") String otpMessage,
                        @RequestParam(value = "authorization", required = false, defaultValue = "false") String authorization,
                        Model model) {
        if(error.equalsIgnoreCase("true")) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if(registered.equalsIgnoreCase("true")) {
            model.addAttribute("registeredMessage", "You've registered successfully");
        }
        if(otpMessage.equalsIgnoreCase("true")) {
            model.addAttribute("otpMessage", "You've verified OTP successfully");
        }
        if(authorization.equalsIgnoreCase("true")) {
            model.addAttribute("authorization", "Sorry, You're not authorized to use this website !!");
        }

        return "login";
    }
}
