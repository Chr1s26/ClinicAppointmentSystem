package com.clinic.appointment.dto.patient;

import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private GenderType genderType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Long appUserId;
    private PatientType patientType;
    private MultipartFile file;
    private String profileUrl;
}
