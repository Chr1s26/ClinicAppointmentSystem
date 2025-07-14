package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE d.id <> :id AND LOWER(d.name) = LOWER(:value)")
    Optional<Doctor> findDoctorByName(@Param("id") Long id, @Param("value") String value);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) = LOWER(:value)")
    Optional<Doctor> findDoctorByNameIgnoreCase(@Param("value") String value);

    @Query("SELECT d FROM Doctor d WHERE d.id <> :id AND d.phone = :value")
    Optional<Doctor> findDoctorByPhone(@Param("id") Long id, @Param("value") String value);

    Optional<Doctor> findByPhone(String phone);

    Optional<Doctor> findDoctorByAppUser(AppUser appUser);
}
