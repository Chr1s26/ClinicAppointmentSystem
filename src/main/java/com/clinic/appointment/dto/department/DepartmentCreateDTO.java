package com.clinic.appointment.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateDTO {

    private Long id;

    @NotBlank(message = "Department name cannot be empty.")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters.")
    private String departmentName;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    private String departmentDescription;
}
