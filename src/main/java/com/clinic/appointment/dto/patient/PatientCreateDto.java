package com.clinic.appointment.dto.patient;

import com.clinic.appointment.model.constant.PatientType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PatientCreateDto {
    private String name;
    private String email;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private PatientType patientType;
    private MultipartFile file;
}
