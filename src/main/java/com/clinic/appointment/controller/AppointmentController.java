package com.clinic.appointment.controller;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchField;
import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchFilter;
import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchQuery;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.service.excelExport.AppointmentExportProcess;
import com.clinic.appointment.service.search.AppointmentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentSearchService searchService;
    private final AppointmentExportProcess exportProcess;

    @ModelAttribute("query")
    public AppointmentSearchQuery initQuery() {
        AppointmentSearchQuery query = new AppointmentSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(10);
        query.setSortBy("appointmentDate");
        query.setSortDirection(SortDirection.DESC);

        query.setFilterList(List.of(
                new AppointmentSearchFilter(AppointmentSearchField.PATIENT_NAME, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.DOCTOR_NAME, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.DEPARTMENT, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.DATE, MatchType.EXACT, ""),
                new AppointmentSearchFilter(AppointmentSearchField.STATUS, MatchType.EXACT, "")
        ));

        return query;
    }

    @GetMapping
    public String list(@ModelAttribute("query") AppointmentSearchQuery query, Model model) {
        Page<Appointment> page = searchService.searchByQuery(query);

        model.addAttribute("appointments", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());

        return "appointments/listing";
    }

    @PostMapping
    public String search(@ModelAttribute("query") AppointmentSearchQuery query, Model model) {
        Page<Appointment> page = searchService.searchByQuery(query);

        model.addAttribute("appointments", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());

        return "appointments/listing";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") AppointmentSearchQuery query) {
        exportProcess.generateExportFile(query);
        return "redirect:/appointments";
    }
}