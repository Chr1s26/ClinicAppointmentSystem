package com.clinic.appointment.controller;

import com.clinic.appointment.dto.DepartmentResponse;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(Model model,
                                    @RequestParam(defaultValue = "0",required = false) Integer pageNumber,
                                    @RequestParam(defaultValue = "9",required = false) Integer pageSize,
                                    @RequestParam(defaultValue = "departmentName",required = false) String sortBy,
                                    @RequestParam(defaultValue = "asc",required = false)String sortOrder){
        DepartmentResponse departments = departmentService.getAllDepartments(pageNumber,pageSize,sortBy,sortOrder);
        model.addAttribute("departments",departments.getDepartments());
        model.addAttribute("response",departments);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        return "departments/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("department",new Department());
        return "departments/create";
    }

    @PostMapping("/create")
    public String createDepartment(@ModelAttribute Department department, Model model){
        departmentService.createDepartment(department,model);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model,@PathVariable Long id){
        model.addAttribute("department",departmentService.findDepartmentById(id));
        return "departments/edit";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department department, Model model){
        departmentService.updateDepartment(id, department,model);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}
