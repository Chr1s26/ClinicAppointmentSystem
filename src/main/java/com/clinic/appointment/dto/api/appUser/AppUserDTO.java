package com.clinic.appointment.dto.api.appUser;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Role;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private String email;
    private Boolean isAccountConfirmed;
    private List<Role> roles;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private AppUser createdBy;
    private AppUser updatedBy;
}
