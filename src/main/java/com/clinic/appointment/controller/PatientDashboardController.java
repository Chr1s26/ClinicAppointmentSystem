package com.clinic.appointment.controller;

import com.clinic.appointment.model.Patient;
import com.clinic.appointment.service.PatientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PatientDashboardController {

    private final PatientDashboardService dashboardService;

    @GetMapping("/patient/dashboard")
    public String dashboard(Model model) {

        Patient patient = dashboardService.getCurrentPatient();

        model.addAttribute("patient", patient);
        model.addAttribute("appointments", dashboardService.getPatientAppointments(patient.getId()));

        return "patients/dashboard";
    }
}