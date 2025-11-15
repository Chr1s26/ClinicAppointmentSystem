package com.clinic.appointment.dto.appUser;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.Role;
import com.clinic.appointment.model.constant.StatusType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
    private LocalDateTime confirmedAt;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppUser createdBy;
    private AppUser updatedBy;
    private Patient patient;
    private String profileUrl;
    private String contentType;
}
