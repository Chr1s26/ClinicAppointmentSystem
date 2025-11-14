package com.clinic.appointment.dto.appUser;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserCreateDTO {

    private Long id;

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 3, max = 50, message = "Username must be 3–50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Username can include letters, numbers, underscore, dash, dot.")
    private String username;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must include at least 8 characters.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/])(?!.*\\s).{8,}$",
            message = "Password must contain upper, lower, digit, symbol, and no spaces"
    )
    private String password;
}
