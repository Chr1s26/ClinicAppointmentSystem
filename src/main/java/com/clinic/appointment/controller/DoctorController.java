package com.clinic.appointment.controller;


import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("doctor",new Doctor());
        return "doctors/create";
    }

    @GetMapping
    public String getDoctors(Model model){
        model.addAttribute("doctors",doctorService.findAll());
        return "doctors/listing";
    }

    @PostMapping("/create")
    public String createDoctor(@ModelAttribute Doctor doctor){
        doctorService.createDoctor(doctor);
        return  "redirect:/doctors";
    }
}
