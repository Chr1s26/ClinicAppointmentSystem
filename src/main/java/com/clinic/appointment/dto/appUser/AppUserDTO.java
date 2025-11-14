package com.clinic.appointment.dto.appUser;

import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean accountConfirmed;
    private LocalDate confirmedAt;
    private String status;
}
