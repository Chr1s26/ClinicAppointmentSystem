package com.clinic.appointment.dto.appointment;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDTO {

    @NotNull
    private Long id;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotBlank(message = "Time slot is required")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "Time slot format: HH:mm-HH:mm")
    private String timeSlot;

    @Size(max = 500, message = "Notes must be at most 500 chars")
    private String notes;
}
