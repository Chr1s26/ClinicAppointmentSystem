package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUsernameIgnoreCase(String parameter);
}
