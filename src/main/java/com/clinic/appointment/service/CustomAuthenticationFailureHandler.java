package com.clinic.appointment.service;

import com.clinic.appointment.exception.AccountNotConfirmedException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private AppUserRepository appUserRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String name = request.getParameter("username");

        if(exception.getCause() instanceof AccountNotConfirmedException) {
            AppUser user = appUserRepository.findByUsernameIgnoreCase(name).orElse(null);
            if(user != null) {
                response.sendRedirect("/confirm-account/otp?email=" + user.getEmail() + "&error=unconfirmed&message=" + "Please Verify the OTP!!");
                return;
            }
        }
        response.sendRedirect("/login?error=true");

    }
}
