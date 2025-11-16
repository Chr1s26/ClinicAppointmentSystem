package com.clinic.appointment.repository;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long>,
        JpaSpecificationExecutor<DoctorSchedule> {

    Optional<DoctorSchedule> findByDoctorAndDayOfWeekIgnoreCase(Doctor doctor, String day);

    Optional<DoctorSchedule> findByDoctorAndDayOfWeekIgnoreCaseAndIdNot(Doctor doctor, String day, Long id);

    void deleteByDoctorId(Long doctorId);

    List<?> findByDoctorId(Long doctorId);
}
