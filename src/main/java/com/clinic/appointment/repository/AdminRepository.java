package com.clinic.appointment.repository;

import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long>, JpaSpecificationExecutor<Admin> {
    Optional<Admin> findAdminByAppUser(AppUser appUser);

    Optional<Admin> findByPhoneAndIdNot(@NotBlank(message = "Phone cannot be empty.") @Pattern(regexp = "^[0-9\\-\\s()+]{7,20}$", message = "Invalid phone number format.") String phone, Long id);

    Optional<Admin> findByPhone(@NotBlank(message = "Phone cannot be empty.") @Pattern(regexp = "^[0-9\\-\\s()+]{7,20}$", message = "Invalid phone number format.") String phone);

}
