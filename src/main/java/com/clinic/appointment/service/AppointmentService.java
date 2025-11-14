package com.clinic.appointment.service;

import com.clinic.appointment.dto.appointment.*;
import com.clinic.appointment.exception.BadRequestException;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.NotFoundException;
import com.clinic.appointment.model.*;
import com.clinic.appointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    /**
     * Book an appointment. This method:
     *  - validates patient + doctor existence
     *  - obtains a pessimistic lock on the appointment slot (findByIdForUpdate)
     *  - checks slot is free and belongs to doctor and matches date/time
     *  - checks no existing appointment for same doctor/date/time
     *  - marks slot.booked = true and creates Appointment record (transactional)
     */
    @Transactional
    public AppointmentDTO bookAppointment(AppointmentCreateDTO dto, AppUser actor) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        // lock slot row to avoid concurrent booking
        AppointmentSlot slot = appointmentSlotRepository.findByIdForUpdate(dto.getAppointmentSlotId())
                .orElseThrow(() -> new NotFoundException("Appointment slot not found"));

        // Verify slot belongs to same doctor
        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException("Appointment slot does not belong to selected doctor");
        }

        // Verify date/time matches
        if (!slot.getDate().equals(dto.getAppointmentDate()) || !slot.getTimeSlot().equals(dto.getTimeSlot())) {
            throw new BadRequestException("Slot date/time mismatch");
        }

        // Check if slot already booked
        if (slot.isBooked()) {
            throw new DuplicateException("Appointment slot already booked");
        }

        // Check duplicate appointment (prevent double-booking via appointment table)
        Optional<Appointment> existing = appointmentRepository.findByDoctorAndAppointmentDateAndTimeSlot(
                doctor, dto.getAppointmentDate(), dto.getTimeSlot()
        );
        if (existing.isPresent()) {
            throw new DuplicateException("Doctor already has an appointment at this time");
        }

        // Mark slot as booked
        slot.setBooked(true);
        appointmentSlotRepository.save(slot);

        // Create appointment
        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setDepartment(doctor.getDepartments().stream().findFirst().orElse(null)); // choose primary dept or null
        appt.setAppointmentDate(dto.getAppointmentDate());
        appt.setTimeSlot(dto.getTimeSlot());
        appt.setAppointmentStatus(com.clinic.appointment.model.constant.AppointmentStatus.SCHEDULED);
        appt.setReason(dto.getReason());
        appt.setCreatedBy(actor);
        appt.setUpdatedBy(actor);
        appt.setCreatedAt(java.time.LocalDate.now());
        appt.setUpdatedAt(java.time.LocalDate.now());

        Appointment saved = appointmentRepository.save(appt);
        return modelMapper.map(saved, AppointmentDTO.class);
    }

    public AppointmentDTO findById(Long id) {
        Appointment appt = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
        return modelMapper.map(appt, AppointmentDTO.class);
    }

    /**
     * Reschedule appointment:
     * - free old slot
     * - lock and book new slot
     * - update appointment record
     */
    @Transactional
    public AppointmentDTO reschedule(Long appointmentId, AppointmentUpdateDTO dto, AppUser actor) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appt.getAppointmentStatus() == com.clinic.appointment.model.constant.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot reschedule completed appointment");
        }

        // free old slot if exists
        AppointmentSlot oldSlot = appt.getTimeslot();
        if (oldSlot != null) {
            oldSlot.setBooked(false);
            appointmentSlotRepository.save(oldSlot);
        }

        // find new slot and lock
        AppointmentSlot newSlot = appointmentSlotRepository.findByIdForUpdate(dto.getId())
                .orElseThrow(() -> new NotFoundException("New appointment slot not found"));

        // verify belongs to same doctor as appt.doctor
        if (!newSlot.getDoctor().getId().equals(appt.getDoctor().getId())) {
            throw new BadRequestException("New slot does not belong to the same doctor");
        }

        // verify new slot not booked
        if (newSlot.isBooked()) {
            throw new DuplicateException("New slot already booked");
        }

        // check duplicate appointment for new time
        appointmentRepository.findByDoctorAndAppointmentDateAndTimeSlot(
                appt.getDoctor(), dto.getAppointmentDate(), dto.getTimeSlot()
        ).ifPresent(a -> { throw new DuplicateException("Doctor already has an appointment at this time"); });

        // book new slot
        newSlot.setBooked(true);
        appointmentSlotRepository.save(newSlot);

        // update appointment record
        appt.setTimeslot(newSlot);
        appt.setAppointmentDate(dto.getAppointmentDate());
        appt.setTimeSlot(dto.getTimeSlot());
        appt.setNotes(dto.getNotes());
        appt.setUpdatedBy(actor);
        appt.setUpdatedAt(java.time.LocalDate.now());

        Appointment saved = appointmentRepository.save(appt);
        return modelMapper.map(saved, AppointmentDTO.class);
    }

    /**
     * Cancel appointment: free slot and set status CANCELLED
     */
    @Transactional
    public AppointmentDTO cancel(Long appointmentId, AppUser actor) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appt.getAppointmentStatus() == com.clinic.appointment.model.constant.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel completed appointment");
        }

        // free slot
        AppointmentSlot slot = appt.getTimeslot();
        if (slot != null) {
            // lock slot row
            AppointmentSlot locked = appointmentSlotRepository.findByIdForUpdate(slot.getId())
                    .orElse(slot);
            locked.setBooked(false);
            appointmentSlotRepository.save(locked);
        }

        appt.setAppointmentStatus(com.clinic.appointment.model.constant.AppointmentStatus.CANCELLED);
        appt.setUpdatedAt(java.time.LocalDate.now());
        appt.setUpdatedBy(actor);

        Appointment saved = appointmentRepository.save(appt);
        return modelMapper.map(saved, AppointmentDTO.class);
    }
}
