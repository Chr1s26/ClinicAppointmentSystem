package com.clinic.appointment.dto.searchFilter.doctorDepartment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DoctorDepartmentDTO {
    private Long doctorId;
    private List<Long> departmentIds;
}
