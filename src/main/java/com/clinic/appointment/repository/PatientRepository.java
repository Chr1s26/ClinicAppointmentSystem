package com.clinic.appointment.repository;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.id <> :id AND LOWER(p.name) = LOWER(:name)")
    Optional<Patient> findPatientByName(@Param("id") Long id,@Param("name") String name);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Patient> findPatientByNameIgnoreCase(String name);

    @Query("SELECT p FROM Patient p WHERE p.id <> :id AND p.email = :email")
    Optional<Patient> findPatientByEmail(@Param("id") Long id,@Param("email") String email);

    Optional<Patient> findByEmail(String name);
}

