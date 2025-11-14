package com.clinic.appointment.dto.forget_password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailForm {
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format")
    private String email;
}
