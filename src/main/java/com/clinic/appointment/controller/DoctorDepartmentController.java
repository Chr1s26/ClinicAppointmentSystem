package com.clinic.appointment.controller;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.DoctorDepartmentService;
import com.clinic.appointment.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping
public class DoctorDepartmentController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final DoctorDepartmentService doctorDepartmentService;

    @GetMapping("/doctorDepartment/{id}")
    public String showCreateForm(@PathVariable Long id,Model model){
        model.addAttribute("doctor",doctorService.getDoctorById(id));
        model.addAttribute("departments",departmentService.getAllDepartments());
        return "doctorDepartments/listing";
    }

    @PostMapping("/{doctorId}/create/{departmentId}")
    public String addDoctorToDepartment(@PathVariable Long doctorId, @PathVariable Long departmentId){
        Doctor doctor = doctorDepartmentService.addDoctorToDepartment(doctorId, departmentId);
        return "redirect:/api/doctor-department/"+departmentId;
    }
}
