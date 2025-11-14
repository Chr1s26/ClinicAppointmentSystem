package com.clinic.appointment.dto.appointment;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreateDTO {

    private Long id;

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    @NotNull(message = "Slot id is required")
    private Long appointmentSlotId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotBlank(message = "Time slot is required")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "Time slot format: HH:mm-HH:mm")
    private String timeSlot;

    @Size(max = 500, message = "Reason must be at most 500 chars")
    private String reason;
}
