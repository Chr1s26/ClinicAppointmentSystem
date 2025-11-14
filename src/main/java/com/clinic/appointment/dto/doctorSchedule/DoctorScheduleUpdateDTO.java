package com.clinic.appointment.dto.doctorSchedule;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleUpdateDTO {

    private Long id;

    @NotBlank(message = "Day of week cannot be empty")
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$",
            message = "Invalid day of week")
    private String dayOfWeek;

    @NotBlank(message = "Start time required")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Time format must be HH:mm")
    private String startTime;

    @NotBlank(message = "End time required")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Time format must be HH:mm")
    private String endTime;

    private boolean available = true;
}
