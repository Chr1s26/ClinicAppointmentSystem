package com.clinic.appointment.service;

import com.clinic.appointment.exception.AccountNotConfirmedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        String encodedMessage = "Please Verify the OTP!!";
        if(exception.getCause() instanceof AccountNotConfirmedException){
            response.sendRedirect("/confirm-account/otp?email=" + email + "&error=unconfirmed&message=" + encodedMessage);
        }else{
            response.sendRedirect("/login");
        }

    }
}
