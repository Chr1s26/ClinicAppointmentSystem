package com.clinic.appointment.service;

import com.clinic.appointment.dto.appointmentSlot.*;
import com.clinic.appointment.model.*;
import com.clinic.appointment.repository.AppointmentSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentSlotService {

    private final AppointmentSlotRepository slotRepository;

    public List<AppointmentSlot> getAvailableSlots(Long doctorId, LocalDate date) {
        return slotRepository.findByDoctorIdAndDateOrderByTimeSlot(doctorId, date)
                .stream()
                .filter(slot -> !slot.isBooked())
                .toList();
    }

    public List<LocalDate> getAvailableDates(Long doctorId, int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);

        return slotRepository
                .findDistinctDatesByDoctor(doctorId, today, end);
    }
}

