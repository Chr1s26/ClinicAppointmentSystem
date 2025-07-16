package com.clinic.appointment.service;

import com.clinic.appointment.model.AppUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {
    public AppUser getCurrentUser(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        AppUser appUser = (AppUser) session.getAttribute("currentUser");
        return session != null ? (AppUser) session.getAttribute("currentUser") : null;
    }
}
