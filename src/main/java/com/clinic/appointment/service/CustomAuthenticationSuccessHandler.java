package com.clinic.appointment.service;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        AppUser appUser = appUserRepository.findByEmail(email).orElse(null);

        if (appUser != null) {
            request.getSession().setAttribute("currentUser", appUser);
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if(roles.size() > 1){
            request.getSession().setAttribute("userRoles", new ArrayList<>(roles));
            response.sendRedirect("/select-role");
        }else if(roles.contains("ROLE_ADMIN")){
            response.sendRedirect("/admins/dashboard");
        }else if(roles.contains("ROLE_PATIENT")){
            response.sendRedirect("/patients/home");
        } else if(roles.contains("ROLE_DOCTOR")){
            response.sendRedirect("/doctors/dashboard");
        } else {
            response.sendRedirect("/");
        }
    }
}
