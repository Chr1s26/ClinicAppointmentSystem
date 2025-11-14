package com.clinic.appointment.service;

import com.clinic.appointment.dto.appointmentSlot.*;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.NotFoundException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.*;
import com.clinic.appointment.repository.AppointmentSlotRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AppointmentSlotService {

    private final AppointmentSlotRepository appointmentSlotRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ModelMapper modelMapper;

    public AppointmentSlotDTO findById(Long id) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment slot not found"));
        return modelMapper.map(slot, AppointmentSlotDTO.class);
    }

    public AppointmentSlotDTO create(AppointmentSlotCreateDTO dto, AppUser user) {

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Duplicate slot check
        appointmentSlotRepository.findByDoctorAndDateAndTimeSlot(doctor, dto.getDate(), dto.getTimeSlot())
                .ifPresent(s -> { throw new DuplicateException("Slot already exists for this doctor at this time"); });

        // Validate slot fits doctor schedule
        validateSlotWithinSchedule(doctor, dto.getDate(), dto.getTimeSlot());

        AppointmentSlot slot = new AppointmentSlot();
        slot.setDoctor(doctor);
        slot.setDate(dto.getDate());
        slot.setTimeSlot(dto.getTimeSlot());
        slot.setBooked(dto.isBooked());
        slot.setCreatedAt(LocalDate.now());
        slot.setUpdatedAt(LocalDate.now());
        slot.setCreatedBy(user);
        slot.setUpdatedBy(user);
        slot.setStatus("ACTIVE");

        AppointmentSlot saved = appointmentSlotRepository.save(slot);
        return modelMapper.map(saved, AppointmentSlotDTO.class);
    }

    public AppointmentSlotDTO update(Long id, AppointmentSlotUpdateDTO dto, AppUser user) {

        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment slot not found"));

        appointmentSlotRepository
                .findByDoctorAndDateAndTimeSlotAndIdNot(slot.getDoctor(), dto.getDate(), dto.getTimeSlot(), id)
                .ifPresent(s -> { throw new DuplicateException("Slot already exists for this doctor at this time"); });

        validateSlotWithinSchedule(slot.getDoctor(), dto.getDate(), dto.getTimeSlot());

        slot.setDate(dto.getDate());
        slot.setTimeSlot(dto.getTimeSlot());
        slot.setBooked(dto.isBooked());
        slot.setUpdatedAt(LocalDate.now());
        slot.setUpdatedBy(user);

        AppointmentSlot saved = appointmentSlotRepository.save(slot);
        return modelMapper.map(saved, AppointmentSlotDTO.class);
    }

    public void softDelete(Long id) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment slot not found"));
        slot.setStatus("DELETE");
        appointmentSlotRepository.save(slot);
    }

    private void validateSlotWithinSchedule(Doctor doctor, LocalDate date, String timeSlot) {
        String[] parts = timeSlot.split("-");
        String slotStart = parts[0];
        String slotEnd = parts[1];

        var scheduleOpt = doctorScheduleRepository.findByDoctorAndDayOfWeekIgnoreCase(
                doctor, date.getDayOfWeek().name()
        );

        if (scheduleOpt.isEmpty())
            throw new ResourceNotFoundException("Doctor has no schedule on " + date.getDayOfWeek());

        DoctorSchedule schedule = scheduleOpt.get();

        if (slotStart.compareTo(schedule.getStartTime()) < 0 ||
                slotEnd.compareTo(schedule.getEndTime()) > 0) {
            throw new DuplicateException("Slot time does not fit inside doctor's working hours");
        }
    }
}
