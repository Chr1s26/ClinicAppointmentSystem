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

        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(slot.getDoctor());
        appt.setDepartment(slot.getDoctor().getDepartments().stream().findFirst().get());
        appt.setAppointmentDate(slot.getDate());
        appt.setTimeSlot(slot.getTimeSlot());
        appt.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appt.setCreatedBy(actor);

        Appointment saved = appointmentRepository.save(appt);
        return modelMapper.map(saved, AppointmentDTO.class);
    }

    public void updateStatus(Long id, AppointmentStatus status) {

        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "appointment", id, "id", "appointments/listing", "Appointment not found"
                ));

        appt.setAppointmentStatus(status);
        appointmentRepository.save(appt);
    }
}
