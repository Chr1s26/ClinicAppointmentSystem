package com.clinic.appointment.dto.admin;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Long id;
    private String name;
    private String phone;
    private AppUser appUser;
    private GenderType genderType;
    private String address;
    private LocalDate dateOfBirth;

    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppUser createdBy;
    private AppUser updatedBy;
}
