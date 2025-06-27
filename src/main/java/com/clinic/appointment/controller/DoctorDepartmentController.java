package com.clinic.appointment.controller;

import com.clinic.appointment.dto.DepartmentResponse;
import com.clinic.appointment.dto.DoctorDTO;
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
    public String showCreateForm(@PathVariable Long id,Model model,
                                 @RequestParam(defaultValue = "0",required = false) Integer pageNumber,
                                 @RequestParam(defaultValue = "9",required = false) Integer pageSize,
                                 @RequestParam(defaultValue = "departmentName",required = false) String sortBy,
                                 @RequestParam(defaultValue = "asc",required = false)String sortOrder){
        DepartmentResponse  departments = doctorDepartmentService.getAllDepartmentsByDoctorId(id,pageNumber,pageSize,sortBy,sortOrder);
        DoctorDTO doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor",doctor);
        model.addAttribute("departments",departments.getDepartments());
        model.addAttribute("response",departments);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "doctorDepartments/listing";
    }

    @PostMapping("/doctor/{doctorId}/department/{departmentId}/join")
    public String addDoctorToDepartment(@PathVariable Long doctorId, @PathVariable Long departmentId){
        Doctor doctor = doctorDepartmentService.addDoctorToDepartment(doctorId, departmentId);
        return "redirect:/doctorDepartment/" + doctorId;
    }
}
