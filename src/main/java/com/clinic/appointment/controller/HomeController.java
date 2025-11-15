package com.clinic.appointment.controller;

import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("totalDoctor",doctorRepository.count());
        model.addAttribute("totalPatient",patientRepository.count());
        model.addAttribute("totalDepartment",departmentRepository.count());
        model.addAttribute("totalAppointment",appointmentRepository.count());
        return "home";
    }
}
