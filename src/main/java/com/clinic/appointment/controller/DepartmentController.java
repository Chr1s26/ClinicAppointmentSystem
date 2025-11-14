package com.clinic.appointment.controller;

import com.clinic.appointment.dto.department.DepartmentCreateDTO;
import com.clinic.appointment.dto.department.DepartmentDTO;
import com.clinic.appointment.dto.department.DepartmentUpdateDTO;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchField;
import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchFilter;
import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchQuery;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.service.DepartmentService;
import com.clinic.appointment.service.excelExport.DepartmentExportProcess;
import com.clinic.appointment.service.search.DepartmentSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentSearchService departmentSearchService;
    private final DepartmentExportProcess departmentExportProcess;

    @ModelAttribute("query")
    public DepartmentSearchQuery initQuery() {
        DepartmentSearchQuery query = new DepartmentSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(6);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new DepartmentSearchFilter(DepartmentSearchField.NAME, MatchType.CONTAINS, ""),
                new DepartmentSearchFilter(DepartmentSearchField.DESCRIPTION, MatchType.CONTAINS, ""),
                new DepartmentSearchFilter(DepartmentSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllDepartments(Model model, @ModelAttribute("query") DepartmentSearchQuery query) {
        Page<Department> page = departmentSearchService.searchByQuery(query);
        model.addAttribute("departments", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        return "departments/listing";
    }

    @PostMapping
    public String searchDepartments(Model model, @ModelAttribute("query") DepartmentSearchQuery query) {
        Page<Department> page = departmentSearchService.searchByQuery(query);
        model.addAttribute("departments", page.getContent());
        return "departments/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new DepartmentCreateDTO());
        return "departments/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("department") DepartmentCreateDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("department", new DepartmentCreateDTO());
            return "departments/create";
        }
        departmentService.createDepartment(dto);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        return "departments/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("department") DepartmentUpdateDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "departments/edit";
        }
        departmentService.updateDepartment(id, dto);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }

    @PostMapping("/export/excel")
    public String exportExcel(@ModelAttribute("query") DepartmentSearchQuery query) {
        departmentExportProcess.generateExportFile(query);
        return "redirect:/departments";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable Long id, Model model) {
        DepartmentDTO departmentDTO = departmentService.findDepartmentById(id);
        model.addAttribute("department", departmentDTO);
        return "departments/view";
    }
}

