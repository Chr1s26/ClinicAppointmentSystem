package com.clinic.appointment.dto.doctorSchedule;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleCreateDTO {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private boolean available;
}
