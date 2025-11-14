package com.clinic.appointment.dto.appointmentSlot;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotUpdateDTO {

    @NotNull
    private LocalDate date;

    @NotBlank(message = "Time slot required (e.g. 09:00-10:00)")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$",
            message = "Time slot format: HH:mm-HH:mm")
    private String timeSlot;

    private boolean booked;
}
