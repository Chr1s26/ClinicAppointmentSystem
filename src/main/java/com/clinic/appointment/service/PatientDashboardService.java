package com.clinic.appointment.service;

import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDashboardService {

    private final AuthService authService;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public Patient getCurrentPatient() {

        AppUser user = authService.getCurrentUser();

        return patientRepository.findByAppUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("patient", user.getId(), "appUserId", "patient/dashboard.html", "You do not have a patient profile"));
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateAsc(patientId);
    }
}