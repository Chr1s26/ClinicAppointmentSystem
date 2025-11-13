package com.clinic.appointment.config;

import com.clinic.appointment.dto.ProfileDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestURIInterceptor implements HandlerInterceptor {
    private final ProfileService profileService;
    private final AuthService authService;

    public RequestURIInterceptor(ProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) return;

        AppUser currentUser = authService.getCurrentUser();

        // Not logged in → nothing to add
        if (currentUser == null) return;

        // Ask ProfileService for profile information
        String activeRole = authService.getActiveRole(); // ROLE_ADMIN / ROLE_DOCTOR / ROLE_PATIENT
        ProfileDTO profileDTO = profileService.getProfileUrl(activeRole);

        // Attach to model for header usage
        modelAndView.addObject("profile", profileDTO);
    }
    }
//        if(modelAndView != null) {
//            modelAndView.addObject("requestURI", request.getRequestURI());
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                String activeRole = (String) session.getAttribute("activeRole");
//                if (activeRole != null) {
//                    ProfileDTO profileDTO = profileService.getProfileUrl(activeRole);
//                    modelAndView.addObject("profile", profileDTO);
//                }
//            }
//        }
//    }
//}
