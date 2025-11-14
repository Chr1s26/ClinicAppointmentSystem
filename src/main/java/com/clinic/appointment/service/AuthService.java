package com.clinic.appointment.service;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;

    //rest controller get current user
    public AppUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return appUserRepository.findByEmail(email).orElse(null);
        }
        return null;
    }


    public String getActiveRole() {
        ServletRequestAttributes attr  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        String activeRole = null;
        if(session != null){
            activeRole =(String) session.getAttribute("activeRole");
        }
        return activeRole;
    }

}
