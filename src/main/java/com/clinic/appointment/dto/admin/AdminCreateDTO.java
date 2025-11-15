package com.clinic.appointment.dto.admin;

import com.clinic.appointment.model.constant.GenderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreateDTO {
    private Long id;

    @NotBlank(message = "Admin name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name cannot include numbers or symbols")
    private String name;

    @NotBlank(message = "Phone cannot be empty.")
    @Pattern(regexp = "^[0-9\\-\\s()+]{7,20}$", message = "Invalid phone number format.")
    private String phone;

    @NotNull(message = "Gender must be selected")
    private GenderType genderType;

    @NotNull(message = "App user id required")
    private Long appUserId;

    @NotBlank(message = "Address cannot be empty.")
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth required")
    private LocalDate dateOfBirth;

}
