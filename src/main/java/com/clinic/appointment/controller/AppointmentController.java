package com.clinic.appointment.controller;

import com.clinic.appointment.annotation.ActiveUser;
import com.clinic.appointment.dto.appointment.*;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.appointment.*;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.service.*;
import com.clinic.appointment.service.excelExport.AppointmentExportProcess;
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
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentSearchService appointmentSearchService;
    private final AppointmentExportProcess appointmentExportProcess;

    @ModelAttribute("query")
    public AppointmentSearchQuery initQuery() {
        AppointmentSearchQuery q = new AppointmentSearchQuery();
        q.setPageNumber(0);
        q.setPageSize(10);
        q.setSortBy("appointmentDate");
        q.setSortDirection(SortDirection.DESC);
        q.setFilterList(List.of(
                new AppointmentSearchFilter(AppointmentSearchField.PATIENT_NAME, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.DOCTOR_NAME, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.DATE, MatchType.EXACT, ""),
                new AppointmentSearchFilter(AppointmentSearchField.TIME_SLOT, MatchType.CONTAINS, ""),
                new AppointmentSearchFilter(AppointmentSearchField.STATUS, MatchType.EXACT, "")
        ));
        return q;
    }

    @GetMapping
    public String list(@ModelAttribute("query") AppointmentSearchQuery query, Model model) {
        Page<Appointment> page = appointmentSearchService.searchByQuery(query);
        model.addAttribute("appointments", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        return "appointments/listing";
    }

    @PostMapping
    public String search(@ModelAttribute("query") AppointmentSearchQuery query, Model model) {
        Page<Appointment> page = appointmentSearchService.searchByQuery(query);
        model.addAttribute("appointments", page.getContent());
        return "appointments/listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new AppointmentCreateDTO());
        // include lists: doctors, patients, slots via services in your context
        return "appointments/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("appointment") AppointmentCreateDTO dto,
                         BindingResult result,
                         @ActiveUser AppUser user,
                         Model model) {
        if (result.hasErrors()) {
            return "appointments/create";
        }

        appointmentService.bookAppointment(dto, user);
        return "redirect:/appointments";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.findById(id));
        return "appointments/view";
    }

    @PostMapping("/reschedule/{id}")
    public String reschedule(@PathVariable Long id,
                             @Valid @ModelAttribute("appointment") AppointmentUpdateDTO dto,
                             BindingResult result,
                             @ActiveUser AppUser user,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("appointment", appointmentService.findById(id));
            return "appointments/edit";
        }

        appointmentService.reschedule(id, dto, user);
        return "redirect:/appointments";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, @ActiveUser AppUser user) {
        appointmentService.cancel(id, user);
        return "redirect:/appointments";
    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") AppointmentSearchQuery query) {
        appointmentExportProcess.generateExportFile(query);
        return "redirect:/appointments";
    }
}
