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

    boolean existsByEmailIgnoreCase(@NotBlank(message = "Email cannot be empty.") @Email(message = "Invalid email format.") String email);

    boolean existsByUsernameIgnoreCaseAndIdNot(@NotBlank(message = "Username cannot be empty.") @Size(min = 3, max = 50, message = "Username must be 3–50 characters.") @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Username can include letters, numbers, underscore, dash, dot.") String username, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(@NotBlank(message = "Email cannot be empty.") @Email(message = "Invalid email format.") String email, Long id);
}
