package com.clinic.appointment.repository;

import com.clinic.appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    Optional<Doctor> findByPhoneIgnoreCase(String phone);
    Optional<Doctor> findByPhoneIgnoreCaseAndIdNot(String phone, Long id);

    Optional<Doctor> findByAppUser_EmailIgnoreCase(String email);
    Optional<Doctor> findByAppUser_EmailIgnoreCaseAndIdNot(String email, Long id);

    Optional<Doctor> findByAppUserId(Long userId);
}
