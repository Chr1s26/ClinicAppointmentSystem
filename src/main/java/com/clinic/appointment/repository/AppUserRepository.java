package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmailIgnoreCase(String email);
    Optional<AppUser> findByUsernameIgnoreCase(String username);

    Optional<AppUser> findByEmailIgnoreCaseAndIdNot(String email, Long id);
    Optional<AppUser> findByUsernameIgnoreCaseAndIdNot(String username, Long id);
}
