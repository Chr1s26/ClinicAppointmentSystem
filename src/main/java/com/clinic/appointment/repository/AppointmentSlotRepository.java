package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository
        extends JpaRepository<AppointmentSlot, Long>, JpaSpecificationExecutor<AppointmentSlot> {

    Optional<AppointmentSlot> findByDoctorAndDateAndTimeSlot(Doctor doctor, LocalDate date, String timeSlot);

    Optional<AppointmentSlot> findByDoctorAndDateAndTimeSlotAndIdNot(
            Doctor doctor, LocalDate date, String timeSlot, Long id
    );

    void deleteAllByDoctorId(Long doctorId);

    List<AppointmentSlot> findByDoctorIdAndDateOrderByTimeSlot(Long doctorId, LocalDate date);

    @Query("select distinct s.date from AppointmentSlot s where s.doctor.id = :doctorId and s.date between :start and :end")
    List<LocalDate> findDistinctDatesByDoctor(Long doctorId, LocalDate start, LocalDate end);

    Optional<AppointmentSlot> findById(Long slotId);
}
