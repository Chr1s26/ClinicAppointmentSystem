package com.clinic.appointment.dto.doctorSchedule;

import com.clinic.appointment.model.Doctor;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleDTO {
    private Long id;
    private Doctor doctor;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private boolean available;
}
