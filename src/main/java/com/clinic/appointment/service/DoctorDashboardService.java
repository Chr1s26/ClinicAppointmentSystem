package com.clinic.appointment.service;

import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.appointment.repository.AppointmentSlotRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorDashboardService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final AppointmentSlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthService authService;

    public Doctor getCurrentDoctor() {

        Long userId = authService.getCurrentUser().getId();

        return doctorRepository.findByAppUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("doctor", userId, "appUserId", "doctor/dashboard.html", "You are not assigned to a doctor profile"));
    }

    public List<?> getWeeklySchedule(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId);
    }

    public List<AppointmentSlot> getUpcomingSlots(Long doctorId) {
        return slotRepository.findByDoctorIdAndDateAfterOrderByDateAscTimeSlotAsc(
                doctorId, LocalDate.now().minusDays(1)
        );
    }

    public List<Appointment> getTodayAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(
                doctorId, LocalDate.now()
        );
    }
}
