package com.clinic.appointment.dto.doctor;

import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.StatusType;
import lombok.Data;
import java.time.LocalDate;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private GenderType genderType;
    private Set<Department> departments;
    private AppUser appUser;
    private StatusType status;
}

