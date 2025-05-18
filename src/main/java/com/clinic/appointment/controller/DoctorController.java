package com.clinic.appointment.controller;


import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        Doctor doctor= this.doctorService.getDoctorById(id);
        model.addAttribute("doctor",doctor);
        return "doctors/edit";
    }

    @PostMapping("/update/{id}")
    public String updateDoctor(@PathVariable("id") Long id, @ModelAttribute("doctor") Doctor doctor){
        this.doctorService.updateDoctor(id,doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable("id") Long id){
        this.doctorService.destory(id);
        return "redirect:/doctors";
    }

}
