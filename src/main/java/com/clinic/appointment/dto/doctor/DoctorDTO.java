package com.clinic.appointment.dto.doctor;

import com.clinic.appointment.model.constant.GenderType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private GenderType genderType;
    private int age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String profileUrl;
}
