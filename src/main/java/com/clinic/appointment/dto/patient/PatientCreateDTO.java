package com.clinic.appointment.dto.patient;

import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreateDTO {

    private Long id;

    @NotBlank(message = "Patient name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;

    @NotBlank(message = "Phone cannot be empty.")
    @Pattern(regexp = "^[0-9\\-\\s()+]{7,20}$", message = "Invalid phone number format.")
    private String phone;

    @NotBlank(message = "Address cannot be empty.")
    private String address;

    @NotNull(message = "Date of birth required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must be selected")
    private GenderType genderType;

    @NotNull(message = "Patient type must be selected")
    private PatientType patientType;

    @NotNull(message = "App user id required")
    private Long appUserId;
}
