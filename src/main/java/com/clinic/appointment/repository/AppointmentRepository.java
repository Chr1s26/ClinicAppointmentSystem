package com.clinic.appointment.repository;

import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
    List<Appointment> findByPatient(Patient patient);

    Optional<Appointment> findByDoctorAndAppointmentDateAndTimeSlot(Doctor doctor, LocalDate date, String timeSlot);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate now);

    List<Appointment> findByPatientIdOrderByAppointmentDateAsc(Long patientId);
}
