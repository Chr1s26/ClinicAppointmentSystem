package com.clinic.appointment.controller;

import com.clinic.appointment.dto.admin.AdminCreateDTO;
import com.clinic.appointment.dto.admin.AdminDTO;
import com.clinic.appointment.dto.admin.AdminUpdateDTO;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.admin.AdminSearchField;
import com.clinic.appointment.dto.searchFilter.admin.AdminSearchFilter;
import com.clinic.appointment.dto.searchFilter.admin.AdminSearchQuery;
import com.clinic.appointment.model.Admin;
import com.clinic.appointment.service.AdminService;
import com.clinic.appointment.service.AuthService;
import com.clinic.appointment.service.excelExport.AdminExportProcess;
import com.clinic.appointment.service.search.AdminSearchService;

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
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final AdminSearchService searchService;
    private final AdminExportProcess exportProcess;
    private final AuthService authService;

    @ModelAttribute("query")
    public AdminSearchQuery initQuery() {
        AdminSearchQuery query = new AdminSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(10);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new AdminSearchFilter(AdminSearchField.NAME, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.EMAIL, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.PHONE, MatchType.CONTAINS, ""),
                new AdminSearchFilter(AdminSearchField.STATUS,MatchType.EXACT,"")
        ));
        return query;
    }

    @GetMapping
    public String list(@ModelAttribute("query") AdminSearchQuery query, Model model) {
        Page<Admin> page = searchService.searchByQuery(query);

        model.addAttribute("admins", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());

        return "admins/listing";
    }

    @PostMapping
    public String search(@ModelAttribute("query") AdminSearchQuery query, Model model) {
        Page<Admin> page = searchService.searchByQuery(query);

        model.addAttribute("admins", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());

        return "admins/listing";
    }

    @GetMapping("/new")
    public String showCreate(Model model) {
        model.addAttribute("admin", new AdminCreateDTO());
        return "admins/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("admin") AdminCreateDTO dto,
                         BindingResult br) {

        if (br.hasErrors()) return "admins/create";

        adminService.create(dto);
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        AdminUpdateDTO dto = adminService.findById(id);
        model.addAttribute("admin", dto);
        return "admins/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("admin") AdminUpdateDTO dto,
                         BindingResult br) {

        if (br.hasErrors()) return "admins/edit";

        adminService.update(id, dto);
        return "redirect:/admins";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return "redirect:/admins";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") AdminSearchQuery query) {
        exportProcess.generateExportFile(query);
        return "redirect:/admins";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        AdminDTO dto = adminService.findAdminById(id);
        model.addAttribute("admin", dto);
        return "admins/view";
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard/index";
    }
}
