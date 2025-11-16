package com.clinic.appointment.config;

import com.clinic.appointment.service.CustomAuthenticationFailureHandler;
import com.clinic.appointment.service.CustomAuthenticationSuccessHandler;
import com.clinic.appointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder);
        auth.setHideUserNotFoundExceptions(false);
        return auth;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/confirm-account/otp**","/confirm-account/verify-otp", "/static/assets/**").permitAll()
                        .requestMatchers("/select-role", "/set-active-role").authenticated()
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/appointments/book/**").hasRole("PATIENT")
                        .requestMatchers("/appointments/**").hasAnyRole("ADMIN","DOCTOR")
                        .requestMatchers("/doctor-schedule/**").hasRole("DOCTOR")
                        .requestMatchers("/doctor/dashboard").hasRole("DOCTOR")
                        .requestMatchers("/patient/dashboard").hasRole("PATIENT")
                        .requestMatchers("/exports/**").hasRole("ADMIN")
                        .requestMatchers("/users/**","/doctors/**","/departments/**").hasRole("ADMIN")
                        .requestMatchers("/patients/**").hasAnyRole("DOCTOR","ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));

        return http.build();
    }

    @PostConstruct
    public void enableInheritableThreadLocal() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}