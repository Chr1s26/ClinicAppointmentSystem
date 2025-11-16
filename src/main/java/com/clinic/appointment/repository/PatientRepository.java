package com.clinic.appointment.repository;

import com.clinic.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    Optional<Patient> findByPhoneIgnoreCase(String phone);
    Optional<Patient> findByPhoneIgnoreCaseAndIdNot(String phone, Long id);

    Optional<Patient> findByEmailIgnoreCase(String email);
    Optional<Patient> findByEmailIgnoreCaseAndIdNot(String email, Long id);

    Optional<Patient> findByAppUserId(Long id);
}
