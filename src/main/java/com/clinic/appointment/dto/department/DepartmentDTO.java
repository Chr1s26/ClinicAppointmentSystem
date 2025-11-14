package com.clinic.appointment.dto.department;

import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.StatusType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    private Long id;
    private String departmentName;
    private String departmentDescription;

    private StatusType status;
    private AppUser createdBy;
    private AppUser updatedBy;
}
