package com.clinic.appointment.dto.doctor;

import com.clinic.appointment.model.constant.GenderType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateDTO {

    private Long id;

    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;

    @NotBlank(message = "Phone cannot be empty.")
    @Pattern(regexp = "^[0-9\\-\\s()+]{7,20}$", message = "Invalid phone number format.")
    private String phone;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format")
    private String email;

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must be selected")
    private GenderType genderType;

    @Size(min = 1, message = "Select at least one department")
    private Set<@NotNull(message = "Department id cannot be null") Long> departmentIds;

    @NotNull(message = "App user id required")
    private Long appUserId;
}
