package com.clinic.appointment.controller;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.service.AppointmentService;
import com.clinic.appointment.service.AppointmentSlotService;
import com.clinic.appointment.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments/book")
public class AppointmentBookController {

    private final DoctorRepository doctorRepository;
    private final AppointmentSlotService slotService;
    private final AppointmentService appointmentService;
    private final AuthService authService;

    @GetMapping
    public String chooseDoctor(Model model) {

        AppUser appUser = authService.getCurrentUser();
        if(appUser.getPatient() == null) {
            return "redirect:/profile/new";
        }

        model.addAttribute("doctors", doctorRepository.findAll());
        return "appointments/select-doctor";
    }

    @GetMapping("/{doctorId}")
    public String chooseDate(@PathVariable Long doctorId, Model model) {

        List<LocalDate> dates = slotService.getAvailableDates(doctorId, 7);

        model.addAttribute("doctorId", doctorId);
        model.addAttribute("dates", dates);

        return "appointments/select-date";
    }

    @GetMapping("/{doctorId}/{date}")
    public String chooseSlot(@PathVariable Long doctorId, @PathVariable String date, Model model) {

        LocalDate d = LocalDate.parse(date);

        List<AppointmentSlot> slots = slotService.getAvailableSlots(doctorId, d);

        model.addAttribute("slots", slots);
        return "appointments/select-slot";
    }

    @PostMapping("/confirm/{slotId}")
    public String confirmBooking(@PathVariable Long slotId) {

        Long patientId = authService.getCurrentUser().getPatient().getId();

        appointmentService.bookSlot(slotId, patientId, authService.getCurrentUser());

        return "redirect:/patient/dashboard";
    }
}
