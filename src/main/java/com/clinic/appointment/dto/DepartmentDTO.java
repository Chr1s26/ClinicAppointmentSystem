package com.clinic.appointment.dto;

import lombok.AllArgsConstructor;
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

    public DepartmentDTO(Long id, String departmentName, String departmentDescription, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.departmentName = departmentName;
        this.departmentDescription = departmentDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public DepartmentDTO(Long id, String departmentName,String departmentDescription, LocalDate createdAt, LocalDate updatedAt, boolean isJoined ) {
        this.id = id;
        this.departmentName = departmentName;
        this.departmentDescription = departmentDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isJoined = isJoined;
    }

    public void setJoined(boolean joined) {
        this.isJoined = joined;
    }

    public boolean isJoined() {
        return this.isJoined;
    }
}
