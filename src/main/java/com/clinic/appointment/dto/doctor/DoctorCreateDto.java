package com.clinic.appointment.dto.doctor;

import com.clinic.appointment.model.constant.GenderType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class DoctorCreateDto {
    private String name;
    private String phone;
    private String address;
    private GenderType genderType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private MultipartFile file;
}
