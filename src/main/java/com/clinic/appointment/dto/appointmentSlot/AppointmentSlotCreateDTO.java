package com.clinic.appointment.dto.appointmentSlot;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotCreateDTO {

    private Long id;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Time slot required (e.g. 09:00-10:00)")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$",
            message = "Time slot format: HH:mm-HH:mm")
    private String timeSlot;

    private boolean booked = false;
}
