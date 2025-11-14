package com.clinic.appointment.service;

import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleCreateDTO;
import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleDTO;
import com.clinic.appointment.dto.doctorSchedule.DoctorScheduleUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.DoctorSchedule;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public DoctorScheduleDTO findById(Long id) {
        DoctorSchedule ds = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return modelMapper.map(ds, DoctorScheduleDTO.class);
    }

    public DoctorScheduleDTO create(DoctorScheduleCreateDTO dto, AppUser user) {

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Duplicate check: doctor can only have one schedule per day
        doctorScheduleRepository.findByDoctorAndDayOfWeekIgnoreCase(doctor, dto.getDayOfWeek())
                .ifPresent(s -> { throw new DuplicateException("Doctor already has schedule on this day"); });

        DoctorSchedule ds = new DoctorSchedule();
        ds.setDoctor(doctor);
        ds.setDayOfWeek(dto.getDayOfWeek());
        ds.setStartTime(dto.getStartTime());
        ds.setEndTime(dto.getEndTime());
        ds.setAvailable(dto.isAvailable());
        ds.setCreatedAt(LocalDate.now());
        ds.setUpdatedAt(LocalDate.now());
        ds.setCreatedBy(user);
        ds.setUpdatedBy(user);
        ds.setStatus("ACTIVE");

        DoctorSchedule saved = doctorScheduleRepository.save(ds);
        return modelMapper.map(saved, DoctorScheduleDTO.class);
    }

    public DoctorScheduleDTO update(Long id, DoctorScheduleUpdateDTO dto, AppUser user) {

        DoctorSchedule ds = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        doctorScheduleRepository
                .findByDoctorAndDayOfWeekIgnoreCaseAndIdNot(ds.getDoctor(), dto.getDayOfWeek(), id)
                .ifPresent(s -> { throw new DuplicateException("Doctor already has schedule on this day"); });

        ds.setDayOfWeek(dto.getDayOfWeek());
        ds.setStartTime(dto.getStartTime());
        ds.setEndTime(dto.getEndTime());
        ds.setAvailable(dto.isAvailable());
        ds.setUpdatedAt(LocalDate.now());
        ds.setUpdatedBy(user);

        DoctorSchedule saved = doctorScheduleRepository.save(ds);
        return modelMapper.map(saved, DoctorScheduleDTO.class);
    }

    public void softDelete(Long id) {
        DoctorSchedule ds = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        ds.setStatus("DELETE");
        doctorScheduleRepository.save(ds);
    }
}
