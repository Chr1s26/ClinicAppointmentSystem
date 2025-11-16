package com.clinic.appointment.controller;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DoctorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DoctorDashBoardController {

    private final DoctorDashboardService dashboardService;

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model) {

        Doctor doctor = dashboardService.getCurrentDoctor();

        model.addAttribute("doctor", doctor);
        model.addAttribute("weekly", dashboardService.getWeeklySchedule(doctor.getId()));
        model.addAttribute("slots", dashboardService.getUpcomingSlots(doctor.getId()));
        model.addAttribute("todayAppointments", dashboardService.getTodayAppointments(doctor.getId()));

        return "doctors/dashboard";
    }
}
