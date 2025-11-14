package com.clinic.appointment.service;

import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //rest controller get current user
//    public AppUser getCurrentUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
//            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
//            return appUserRepository.findByEmail(email).orElse(null);
//        }
//        return null;
//    }

    public AppUser getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return appUserRepository.findById(userDetails.getId()).orElse(null);
        }

        return null;
    }


    public void resetPassword(String email, String password) {
        Optional<AppUser> userOp = appUserRepository.findByEmail(email);
        AppUser user = userOp.get();
        if (userOp.isPresent()) {
            user.setPassword(passwordEncoder.encode(password));
            appUserRepository.save(user);
        }else{
            throw new ResourceNotFoundException("appUser",email,"email","forget-password","User with email ( "+email+" ) doesn't exist");
        }
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
