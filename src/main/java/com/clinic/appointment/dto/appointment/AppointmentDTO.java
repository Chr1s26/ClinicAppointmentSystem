package com.clinic.appointment.dto.appointment;

import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Patient;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private String departmentName;
    private LocalDate appointmentDate;
    private String timeSlot; // e.g. "09:00-09:30"
    private String appointmentStatus; // SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
    private String reason;
    private String notes;
}
