package com.clinic.appointment.dto.appointmentSlot;

import com.clinic.appointment.model.Doctor;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotDTO {
    private Long id;
    private LocalDate date;
    private String timeSlot;
    private boolean booked;
    private Doctor doctor;
}
