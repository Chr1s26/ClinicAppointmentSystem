package com.clinic.appointment.controller;

import com.clinic.appointment.dto.doctor.DoctorCreateDTO;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorUpdateDTO;
import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchFilter;
import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchField;
import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchQuery;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.service.DoctorService;
import com.clinic.appointment.service.excelExport.DoctorExportProcess;
import com.clinic.appointment.service.search.DoctorSearchService;
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
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorSearchService doctorSearchService;
    private final DoctorExportProcess doctorExportProcess;

    @ModelAttribute("query")
    public DoctorSearchQuery initQuery() {
        DoctorSearchQuery q = new DoctorSearchQuery();
        q.setPageNumber(0);
        q.setPageSize(6);
        q.setSortBy("createdAt");
        q.setSortDirection(SortDirection.DESC);
        q.setFilterList(List.of(
                new DoctorSearchFilter(DoctorSearchField.NAME, MatchType.CONTAINS, ""),
                new DoctorSearchFilter(DoctorSearchField.PHONE, MatchType.CONTAINS, ""),
                new DoctorSearchFilter(DoctorSearchField.EMAIL, MatchType.CONTAINS, ""),
                new DoctorSearchFilter(DoctorSearchField.STATUS, MatchType.EXACT, ""),
                new DoctorSearchFilter(DoctorSearchField.DEPARTMENT, MatchType.EXACT, ""),
                new DoctorSearchFilter(DoctorSearchField.GENDER, MatchType.EXACT, "")
        ));
        return q;
    }

    @GetMapping
    public String list(Model model, @ModelAttribute("query") DoctorSearchQuery query) {
        Page<Doctor> page = doctorSearchService.searchByQuery(query);
        model.addAttribute("doctors", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "doctors/listing";
    }

    @PostMapping
    public String search(Model model, @ModelAttribute("query") DoctorSearchQuery query) {
        Page<Doctor> page = doctorSearchService.searchByQuery(query);
        model.addAttribute("doctors", page.getContent());
        return "doctors/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new DoctorCreateDTO());
        // also include department list, appUser list etc via services in your context
        return "doctors/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("doctor") DoctorCreateDTO dto,
                         BindingResult result,
                         Model model,
                         @ActiveUser AppUser user) {

        if (result.hasErrors()) {
            return "doctors/create";
        }

        doctorService.create(dto, user);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        DoctorDTO dto = doctorService.findById(id);
        model.addAttribute("doctor", dto);
        return "doctors/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("doctor") DoctorUpdateDTO dto,
                         BindingResult result,
                         Model model,
                         AppUser user) {

        if (result.hasErrors()) {
            return "doctors/edit";
        }

        doctorService.update(id, dto, user);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        doctorService.softDelete(id);
        return "redirect:/doctors";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") DoctorSearchQuery query) {
        doctorExportProcess.generateExportFile(query);
        return "redirect:/doctors";
    }
}
