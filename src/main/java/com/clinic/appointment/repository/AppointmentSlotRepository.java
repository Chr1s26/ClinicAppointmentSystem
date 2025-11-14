package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Optional;

public interface AppointmentSlotRepository
        extends JpaRepository<AppointmentSlot, Long>, JpaSpecificationExecutor<AppointmentSlot> {

    Optional<AppointmentSlot> findByDoctorAndDateAndTimeSlot(Doctor doctor, LocalDate date, String timeSlot);

    Optional<AppointmentSlot> findByDoctorAndDateAndTimeSlotAndIdNot(
            Doctor doctor, LocalDate date, String timeSlot, Long id
    );

    // Pessimistic lock to prevent concurrent bookings
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AppointmentSlot s WHERE s.id = :id")
    Optional<AppointmentSlot> findByIdForUpdate(@Param("id") Long id);
}
