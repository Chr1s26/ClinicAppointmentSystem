package com.clinic.appointment.dto.department;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private String departmentDescription;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isJoined;

}
