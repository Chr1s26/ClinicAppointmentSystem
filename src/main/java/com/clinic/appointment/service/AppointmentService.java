package com.clinic.appointment.service;

import com.clinic.appointment.dto.appointment.*;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.*;
import com.clinic.appointment.model.constant.AppointmentStatus;
import com.clinic.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final AppointmentSlotRepository slotRepository;

    public AppointmentDTO bookSlot(Long slotId, Long patientId, AppUser actor) {

        AppointmentSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment", slotId, "slotId", "appointments/booking", "Slot not found"));

        if (slot.isBooked()) {
            throw new DuplicateException("appointment", slot, "timeSlot", "appointments/booking", "Slot already booked");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment", patientId, "patientId", "appointments/booking", "Patient not found"));

        slot.setBooked(true);
        slotRepository.save(slot);

        Appointment appt = Appointment.builder()
                .patient(patient)
                .doctor(slot.getDoctor())
                .department(slot.getDoctor().getDepartments().stream().findFirst().orElse(null))
                .appointmentDate(slot.getDate())
                .timeSlot(slot.getTimeSlot())
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .createdBy(actor)
                .build();

        Appointment saved = appointmentRepository.save(appt);
        return modelMapper.map(saved, AppointmentDTO.class);
    }
}
