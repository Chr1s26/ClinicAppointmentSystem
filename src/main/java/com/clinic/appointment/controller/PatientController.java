package com.clinic.appointment.controller;

import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.dto.patient.PatientDTO;
import com.clinic.appointment.dto.patient.PatientUpdateDTO;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchFilter;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchField;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchQuery;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.service.AppUserService;
import com.clinic.appointment.service.PatientService;
import com.clinic.appointment.service.search.PatientSearchService;
import com.clinic.appointment.service.excelExport.PatientExportProcess;

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
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientSearchService searchService;
    private final PatientExportProcess patientExportProcess;
    private final AppUserService appUserService;

    @ModelAttribute("query")
    public PatientSearchQuery initQuery() {
        PatientSearchQuery query = new PatientSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(10);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new PatientSearchFilter(PatientSearchField.NAME, MatchType.CONTAINS, ""),
                new PatientSearchFilter(PatientSearchField.PHONE, MatchType.CONTAINS, ""),
                new PatientSearchFilter(PatientSearchField.GENDER, MatchType.EXACT, ""),
                new PatientSearchFilter(PatientSearchField.PATIENT_TYPE, MatchType.EXACT, ""),
                new PatientSearchFilter(PatientSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllPatients(@ModelAttribute("query") PatientSearchQuery query, Model model) {
        Page<Patient> page = searchService.searchByQuery(query);
        model.addAttribute("patients", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "patients/listing";
    }

    @PostMapping
    public String searchPatients(@ModelAttribute("query") PatientSearchQuery query, Model model) {
        Page<Patient> page = searchService.searchByQuery(query);
        model.addAttribute("patients", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "patients/listing";
    }

    @GetMapping("/new")
    public String showCreate(Model model) {
        model.addAttribute("patient", new PatientCreateDTO());
        model.addAttribute("appUsers", appUserService.findAllUsers());
        model.addAttribute("genderType", GenderType.values());
        model.addAttribute("patientType", PatientType.values());
        return "patients/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("patient") PatientCreateDTO dto,
                         BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("appUsers", appUserService.findAllUsers());
            model.addAttribute("genderType", GenderType.values());
            model.addAttribute("patientType", PatientType.values());
            return "patients/create";
        }
        patientService.create(dto);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        PatientDTO patient = patientService.findPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("genderType", GenderType.values());
        model.addAttribute("patientType", PatientType.values());
        return "patients/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patient") PatientUpdateDTO dto,
                         BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("genderType", GenderType.values());
            model.addAttribute("patientType", PatientType.values());
            return "patients/edit";
        }
        patientService.update(id, dto);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        patientService.delete(id);
        return "redirect:/patients";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") PatientSearchQuery query) {
        patientExportProcess.generateExportFile(query);
        return "redirect:/patients";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable("id") Long id, Model model) {
        PatientDTO patient = patientService.findPatientById(id);
        model.addAttribute("patient", patient);
        return "patients/view";
    }

}
