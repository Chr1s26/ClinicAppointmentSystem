package com.clinic.appointment.dto.department;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.StatusType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    private Long id;
    private String departmentName;
    private String departmentDescription;
    private StatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppUser createdBy;
    private AppUser updatedBy;
}
