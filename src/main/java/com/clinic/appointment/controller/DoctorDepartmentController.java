package com.clinic.appointment.controller;

import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.searchFilter.doctorDepartment.DoctorDepartmentDTO;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.DoctorDepartmentService;
import com.clinic.appointment.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping
public class DoctorDepartmentController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final DoctorDepartmentService doctorDepartmentService;

    @GetMapping("/doctorDepartment/{id}")
    public String showCreateForm(@PathVariable Long id,Model model){
        DoctorDTO doctorDTO = doctorService.findById(id);
        model.addAttribute("doctor",doctorDTO);
        model.addAttribute("departments", departmentService.findAllDepartments());
        return "doctorDepartments/listing";
    }

    @PostMapping("/doctorDepartment")
    public String addDoctorToDepartment(@ModelAttribute DoctorDepartmentDTO dto){
        Doctor doctor = doctorDepartmentService.addDoctorToDepartment(dto);
        return "redirect:/doctors";
    }
}
