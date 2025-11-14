package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleCreateDTO;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.DoctorSchedule;
import com.clinic.appointment.repository.AppointmentSlotRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final AppointmentSlotRepository slotRepository;

    private static final int SLOT_MINUTES = 30;
    private static final int DAYS_TO_GENERATE = 7;

    @Transactional
    public void saveSchedule(Long doctorId, List<DoctorScheduleCreateDTO> scheduleList) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "doctorSchedule", scheduleList, "doctorId",
                        "doctorSchedule/create", "Doctor not found"
                ));

        scheduleRepository.deleteByDoctorId(doctorId);

        List<DoctorSchedule> saved = new ArrayList<>();
        for (DoctorScheduleCreateDTO dto : scheduleList) {
            DoctorSchedule schedule = DoctorSchedule.builder()
                    .doctor(doctor)
                    .dayOfWeek(dto.getDayOfWeek())
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .available(dto.isAvailable())
                    .build();
            saved.add(scheduleRepository.save(schedule));
        }

        regenerateSlots(doctor, saved);
    }

    @Transactional
    public void regenerateSlots(Doctor doctor, List<DoctorSchedule> schedules) {

        slotRepository.deleteAllByDoctorId(doctor.getId());

        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(DAYS_TO_GENERATE);

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {

            String day = date.getDayOfWeek().name(); // MONDAY, TUESDAY, …

            LocalDate finalDate = date;
            schedules.stream()
                    .filter(s -> s.isAvailable() && s.getDayOfWeek().equalsIgnoreCase(day))
                    .forEach(s -> generateDaySlots(doctor, finalDate, s));
        }
    }

    private void generateDaySlots(Doctor doctor, LocalDate date, DoctorSchedule schedule) {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime start = LocalTime.parse(schedule.getStartTime(), f);
        LocalTime end = LocalTime.parse(schedule.getEndTime(), f);

        LocalTime time = start;

        while (time.plusMinutes(SLOT_MINUTES).compareTo(end) <= 0) {

            String slotString = time.format(f) + "-" + time.plusMinutes(SLOT_MINUTES).format(f);

            AppointmentSlot slot = AppointmentSlot.builder()
                    .doctor(doctor)
                    .date(date)
                    .timeSlot(slotString)
                    .booked(false)
                    .build();

            slotRepository.save(slot);

            time = time.plusMinutes(SLOT_MINUTES);
        }
    }

}
