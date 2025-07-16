package com.clinic.appointment.config;

import com.clinic.appointment.dto.ProfileDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestURIInterceptor implements HandlerInterceptor {
    private final ProfileService profileService;

    public RequestURIInterceptor(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            modelAndView.addObject("requestURI", request.getRequestURI());
            HttpSession session = request.getSession(false);
            if (session != null) {
                String activeRole = (String) session.getAttribute("activeRole");
                if (activeRole != null) {
                    ProfileDTO profileDTO = profileService.getProfileUrl(activeRole);
                    modelAndView.addObject("profile", profileDTO);
                }
            }
        }
    }
}
