package com.clinic.appointment.repository;

import com.clinic.appointment.model.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByEmailIgnoreCase(String email);
    Optional<AppUser> findByUsernameIgnoreCase(String username);

    Optional<AppUser> findByEmailIgnoreCaseAndIdNot(String email, Long id);
    Optional<AppUser> findByUsernameIgnoreCaseAndIdNot(String username, Long id);

    Optional<AppUser> findByEmail(String parameter);

    boolean existsByUsernameIgnoreCaseAndEmail(String username,String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmail(String email);

    Optional<AppUser> findByUsernameIgnoreCaseAndEmailAndIdNot(String username,String email, Long id);
}
