//package com.clinic.appointment.controller;
//
//import com.clinic.appointment.dto.appointmentSlot.*;
//import com.clinic.appointment.dto.searchFilter.*;
//import com.clinic.appointment.dto.searchFilter.appointmentSlot.*;
//import com.clinic.appointment.model.AppUser;
//import com.clinic.appointment.model.AppointmentSlot;
//import com.clinic.appointment.service.*;
//import com.clinic.appointment.service.excelExport.AppointmentSlotExportProcess;
//import jakarta.validation.Valid;
//import lombok.*;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/appointment-slots")
//public class AppointmentSlotController {
//
//    private final AppointmentSlotService appointmentSlotService;
//    private final AppointmentSlotSearchService appointmentSlotSearchService;
//    private final AppointmentSlotExportProcess exportProcess;
//
//    @ModelAttribute("query")
//    public AppointmentSlotSearchQuery initQuery() {
//        AppointmentSlotSearchQuery q = new AppointmentSlotSearchQuery();
//        q.setPageNumber(0);
//        q.setPageSize(6);
//        q.setSortBy("createdAt");
//        q.setSortDirection(SortDirection.DESC);
//        q.setFilterList(List.of(
//                new AppointmentSlotSearchFilter(AppointmentSlotSearchField.DOCTOR_NAME, MatchType.CONTAINS, ""),
//                new AppointmentSlotSearchFilter(AppointmentSlotSearchField.DATE, MatchType.EXACT, ""),
//                new AppointmentSlotSearchFilter(AppointmentSlotSearchField.TIME_SLOT, MatchType.CONTAINS, ""),
//                new AppointmentSlotSearchFilter(AppointmentSlotSearchField.BOOKED, MatchType.EXACT, ""),
//                new AppointmentSlotSearchFilter(AppointmentSlotSearchField.STATUS, MatchType.EXACT, "")
//        ));
//        return q;
//    }
//
//    @GetMapping
//    public String list(@ModelAttribute("query") AppointmentSlotSearchQuery query, Model model) {
//        Page<AppointmentSlot> page = appointmentSlotSearchService.searchByQuery(query);
//        model.addAttribute("slots", page.getContent());
//        model.addAttribute("totalPages", page.getTotalPages());
//        return "appointmentSlots/listing";
//    }
//
//    @PostMapping
//    public String search(@ModelAttribute("query") AppointmentSlotSearchQuery query, Model model) {
//        Page<AppointmentSlot> page = appointmentSlotSearchService.searchByQuery(query);
//        model.addAttribute("slots", page.getContent());
//        return "appointmentSlots/listing";
//    }
//
//    @GetMapping("/new")
//    public String newForm(Model model) {
//        model.addAttribute("slot", new AppointmentSlotCreateDTO());
//        return "appointmentSlots/create";
//    }
//
//    @PostMapping("/create")
//    public String create(@Valid @ModelAttribute("slot") AppointmentSlotCreateDTO dto,
//                         BindingResult result,
//                         @ActiveUser AppUser user) {
//
//        if (result.hasErrors()) return "appointmentSlots/create";
//
//        appointmentSlotService.create(dto, user);
//        return "redirect:/appointment-slots";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editForm(@PathVariable Long id, Model model) {
//        model.addAttribute("slot", appointmentSlotService.findById(id));
//        return "appointmentSlots/edit";
//    }
//
//    @PostMapping("/update/{id}")
//    public String update(@PathVariable Long id,
//                         @Valid @ModelAttribute("slot") AppointmentSlotUpdateDTO dto,
//                         BindingResult result,
//                         @ActiveUser AppUser user) {
//
//        if (result.hasErrors()) return "appointmentSlots/edit";
//
//        appointmentSlotService.update(id, dto, user);
//        return "redirect:/appointment-slots";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        appointmentSlotService.softDelete(id);
//        return "redirect:/appointment-slots";
//    }
//
//    @PostMapping("/export/excel")
//    public String export(@ModelAttribute("query") AppointmentSlotSearchQuery query) {
//        exportProcess.generateExportFile(query);
//        return "redirect:/appointment-slots";
//    }
//}
