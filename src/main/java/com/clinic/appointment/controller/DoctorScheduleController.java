package com.clinic.appointment.controller;

import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleCreateDTO;
import com.clinic.appointment.dto.searchFilter.*;
import com.clinic.appointment.dto.searchFilter.doctorSchedule.*;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.DoctorSchedule;
import com.clinic.appointment.service.*;
import com.clinic.appointment.service.excelExport.DoctorScheduleExportProcess;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor-schedule")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;
//    private final DoctorScheduleSearchService doctorScheduleSearchService;
    private final DoctorScheduleExportProcess exportProcess;
//
//    @ModelAttribute("query")
//    public DoctorScheduleSearchQuery initQuery() {
//        DoctorScheduleSearchQuery q = new DoctorScheduleSearchQuery();
//        q.setPageNumber(0);
//        q.setPageSize(6);
//        q.setSortBy("createdAt");
//        q.setSortDirection(SortDirection.DESC);
//        q.setFilterList(List.of(
//                new DoctorScheduleSearchFilter(DoctorScheduleSearchField.DOCTOR_NAME, MatchType.CONTAINS, ""),
//                new DoctorScheduleSearchFilter(DoctorScheduleSearchField.DAY_OF_WEEK, MatchType.CONTAINS, ""),
//                new DoctorScheduleSearchFilter(DoctorScheduleSearchField.STATUS, MatchType.EXACT, ""),
//                new DoctorScheduleSearchFilter(DoctorScheduleSearchField.AVAILABLE, MatchType.EXACT, "")
//        ));
//        return q;
//    }
//
//    @GetMapping
//    public String list(@ModelAttribute("query") DoctorScheduleSearchQuery query, Model model) {
//        Page<DoctorSchedule> page = doctorScheduleSearchService.searchByQuery(query);
//        model.addAttribute("schedules", page.getContent());
//        model.addAttribute("totalPages", page.getTotalPages());
//        return "doctorSchedule/listing";
//    }
//
//    @PostMapping
//    public String search(@ModelAttribute("query") DoctorScheduleSearchQuery query, Model model) {
//        Page<DoctorSchedule> page = doctorScheduleSearchService.searchByQuery(query);
//        model.addAttribute("schedules", page.getContent());
//        return "doctorSchedule/listing";
//    }
//
//    @GetMapping("/new")
//    public String newForm(Model model) {
//        model.addAttribute("schedule", new DoctorScheduleCreateDTO());
//        return "doctorSchedule/create";
//    }
//
//    @PostMapping("/create")
//    public String create(@Valid @ModelAttribute("schedule") DoctorScheduleCreateDTO dto,
//                         BindingResult result,
//                         @ActiveUser AppUser user) {
//
//        if (result.hasErrors()) return "doctorSchedule/create";
//
//        doctorScheduleService.create(dto, user);
//        return "redirect:/doctor-schedule";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editForm(@PathVariable Long id, Model model) {
//        model.addAttribute("schedule", doctorScheduleService.findById(id));
//        return "doctorSchedule/edit";
//    }
//
//    @PostMapping("/update/{id}")
//    public String update(@PathVariable Long id,
//                         @Valid @ModelAttribute("schedule") DoctorScheduleUpdateDTO dto,
//                         BindingResult result,
//                         @ActiveUser AppUser user) {
//        if (result.hasErrors()) return "doctorSchedule/edit";
//
//        doctorScheduleService.update(id, dto, user);
//        return "redirect:/doctor-schedule";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        doctorScheduleService.softDelete(id);
//        return "redirect:/doctor-schedule";
//    }

    @PostMapping("/export/excel")
    public String export(@ModelAttribute("query") DoctorScheduleSearchQuery query) {
        exportProcess.generateExportFile(query);
        return "redirect:/doctor-schedule";
    }

    @PostMapping("/doctor/{id}/schedule")
    public String saveDoctorSchedule(@PathVariable Long id, @ModelAttribute("schedules") List<DoctorScheduleCreateDTO> list) {
        doctorScheduleService.saveSchedule(id, list);
        return "redirect:/doctors";
    }
}
