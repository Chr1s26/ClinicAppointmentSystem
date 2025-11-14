package com.clinic.appointment.service;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        String parameter = authentication.getName();
        AppUser appUser = appUserRepository.findByEmail(parameter)
                .orElseGet(() -> appUserRepository.findByUsernameIgnoreCase(parameter)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        if (appUser != null) {
            request.getSession().setAttribute("currentUser", appUser);
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        HttpSession session = request.getSession();

        if(roles.size() > 1){
            request.getSession().setAttribute("userRoles", new ArrayList<>(roles));
            response.sendRedirect("/select-role");
        }else if(roles.contains("ROLE_ADMIN")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_ADMIN");
            response.sendRedirect("/home");
        }else if(roles.contains("ROLE_PATIENT")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_PATIENT");
            response.sendRedirect("/home");
        } else if(roles.contains("ROLE_DOCTOR")){
            session.setAttribute("userRoles", new ArrayList<>(roles));
            session.setAttribute("activeRole", "ROLE_DOCTOR");
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/login?authorization=true");
        }
    }
}
