package com.clinic.appointment.dto;

import com.clinic.appointment.model.GenderType;
import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private GenderType genderType;
}
