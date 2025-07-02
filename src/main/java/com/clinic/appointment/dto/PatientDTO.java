package com.clinic.appointment.dto;

import com.clinic.appointment.model.constant.PatientType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PatientDTO {

    private Long id;
    private String name;
    private String email;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private PatientType patientType;
}
