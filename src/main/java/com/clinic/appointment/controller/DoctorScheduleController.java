package com.clinic.appointment.controller;

import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleCreateDTO;
import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleWrapper;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.service.*;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor-schedule")
public class DoctorScheduleController {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleService scheduleService;

    @GetMapping("/{doctorId}")
    public String showScheduleForm(@PathVariable Long doctorId, Model model) {

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<DoctorScheduleCreateDTO> weekly = new ArrayList<>();
        List<String> days = List.of("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY");

        for (String d : days) {
            weekly.add(new DoctorScheduleCreateDTO(d, "09:00", "17:00", true));
        }

        DoctorScheduleWrapper wrapper = new DoctorScheduleWrapper();
        wrapper.setSchedules(weekly);

        model.addAttribute("doctor", doctor);
        model.addAttribute("wrapper", wrapper);

        return "doctorSchedule/form";
    }

    @PostMapping("/{doctorId}/save")
    public String saveSchedule(@PathVariable Long doctorId,
                               @ModelAttribute("wrapper") DoctorScheduleWrapper wrapper) {

        scheduleService.saveSchedule(doctorId, wrapper.getSchedules());

        return "redirect:/doctor/dashboard";
    }
}
