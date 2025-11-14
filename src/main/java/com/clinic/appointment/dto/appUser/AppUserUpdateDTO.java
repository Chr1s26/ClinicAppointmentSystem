package com.clinic.appointment.dto.appUser;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserUpdateDTO {

    @NotNull
    private Long id;

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 3, max = 50, message = "Username must be 3–50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Username can include letters, numbers, underscore, dash, dot.")
    private String username;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;

    // Optional password change
    private String newPassword;

    @NotNull(message = "At least one role must be selected")
    private Set<Long> roleIds;
}
