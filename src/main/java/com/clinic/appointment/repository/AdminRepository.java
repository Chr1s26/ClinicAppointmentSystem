package com.clinic.appointment.repository;

import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findAdminByAppUser(AppUser appUser);
}
