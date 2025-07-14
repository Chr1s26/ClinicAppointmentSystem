package com.clinic.appointment.controller;


import com.clinic.appointment.dto.doctor.DoctorCreateDto;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorResponse;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.DoctorService;
import com.clinic.appointment.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/dashboard")
    public String doctorDashboard(Model model){
        return "/doctors/dashboard/index";
    }

    @GetMapping
    public String getAllDoctors(Model model,
                                    @RequestParam(defaultValue = "0",required = false) Integer pageNumber,
                                    @RequestParam(defaultValue = "9",required = false) Integer pageSize,
                                    @RequestParam(defaultValue = "name",required = false) String sortBy,
                                    @RequestParam(defaultValue = "asc",required = false)String sortOrder){
        DoctorResponse doctorResponse = doctorService.getAllDoctors(pageNumber,pageSize,sortBy,sortOrder);
        List<DoctorDTO> safeDoctorsList = new ArrayList<>(doctorResponse.getDoctors());
        model.addAttribute("doctors",safeDoctorsList);
        model.addAttribute("response",doctorResponse);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "doctors/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("doctor",new DoctorCreateDto());
        model.addAttribute("genderType",GenderType.values());
        model.addAttribute("appUsers",appUserRepository.findAll());
        return "doctors/create";
    }

    @PostMapping("/create")
    public String createDoctor(@ModelAttribute DoctorCreateDto doctor,Model model){
        if(doctor.getAppUserId()==null){
            return "redirect:/register";
        }else{
            model.addAttribute("doctor",doctor);
            doctorService.createDoctor(doctor,model);
            return  "redirect:/doctors";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        DoctorDTO doctor= this.doctorService.getDoctorById(id);
        model.addAttribute("genderType", GenderType.values());
        model.addAttribute("doctor",doctor);
        model.addAttribute("appUsers",appUserRepository.findAll());
        return "doctors/edit";
    }

    @PostMapping("/update/{id}")
    public String updateDoctor(@PathVariable("id") Long id, @ModelAttribute("doctor") DoctorDTO doctor,Model model){
        this.doctorService.updateDoctor(id,doctor,model);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable("id") Long id){
        this.doctorService.deleteById(id);
        return "redirect:/doctors";
    }

    @GetMapping("/view")
    public String viewDoctor(Model model){
        model.addAttribute("doctors",doctorService.findAll());
        return "doctors/listing";
    }

}
