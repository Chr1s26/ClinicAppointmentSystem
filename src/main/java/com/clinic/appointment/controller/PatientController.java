package com.clinic.appointment.controller;

import com.clinic.appointment.annotation.ActiveUser;
import com.clinic.appointment.dto.patient.PatientCreateDTO;
import com.clinic.appointment.dto.patient.PatientDTO;
import com.clinic.appointment.dto.patient.PatientUpdateDTO;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchFilter;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchField;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchQuery;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.service.PatientSearchService;
import com.clinic.appointment.service.PatientService;
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
    private final PatientSearchService patientSearchService;
    private final PatientExportProcess patientExportProcess;

    @ModelAttribute("query")
    public PatientSearchQuery initQuery() {
        PatientSearchQuery q = new PatientSearchQuery();
        q.setPageNumber(0);
        q.setPageSize(6);
        q.setSortBy("createdAt");
        q.setSortDirection(SortDirection.DESC);
        q.setFilterList(List.of(
                new PatientSearchFilter(PatientSearchField.NAME, MatchType.CONTAINS, ""),
                new PatientSearchFilter(PatientSearchField.PHONE, MatchType.CONTAINS, ""),
                new PatientSearchFilter(PatientSearchField.EMAIL, MatchType.CONTAINS, ""),
                new PatientSearchFilter(PatientSearchField.GENDER, MatchType.EXACT, ""),
                new PatientSearchFilter(PatientSearchField.PATIENT_TYPE, MatchType.EXACT, ""),
                new PatientSearchFilter(PatientSearchField.STATUS, MatchType.EXACT, "")
        ));
        return q;
    }

    @GetMapping
    public String list(@ModelAttribute("query") PatientSearchQuery query, Model model) {
        Page<Patient> page = patientSearchService.searchByQuery(query);
        model.addAttribute("patients", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        return "patients/listing";
    }

    @PostMapping
    public String search(@ModelAttribute("query") PatientSearchQuery query, Model model) {
        Page<Patient> page = patientSearchService.searchByQuery(query);
        model.addAttribute("patients", page.getContent());
        return "patients/listing";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("patient", new PatientCreateDTO());
        return "patients/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("patient") PatientCreateDTO dto,
                         BindingResult result,
                         @ActiveUser AppUser user,
                         Model model) {

        if (result.hasErrors()) {
            return "patients/create";
        }

        patientService.create(dto, user);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        PatientDTO dto = patientService.findById(id);
        model.addAttribute("patient", dto);
        return "patients/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patient") PatientUpdateDTO dto,
                         BindingResult result,
                         @ActiveUser AppUser user,
                         Model model) {

        if (result.hasErrors()) {
            return "patients/edit";
        }

        patientService.update(id, dto, user);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        patientService.softDelete(id);
        return "redirect:/patients";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") PatientSearchQuery query) {
        patientExportProcess.generateExportFile(query);
        return "redirect:/patients";
    }
}
