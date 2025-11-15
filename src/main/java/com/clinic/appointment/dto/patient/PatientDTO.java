package com.clinic.appointment.dto.patient;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.model.constant.StatusType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private GenderType genderType;
    private PatientType patientType;
    private AppUser appUser;

    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppUser createdBy;
    private AppUser updatedBy;
}
