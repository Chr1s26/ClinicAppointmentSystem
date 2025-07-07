package com.clinic.appointment.dto.department;

import java.time.LocalDate;

public interface DepartmentProjection {
    Long getId();
    String getDepartmentName();
    String getDepartmentDescription();
    LocalDate getCreatedAt();
    LocalDate getUpdatedAt();
    Boolean getIsJoined();
}
