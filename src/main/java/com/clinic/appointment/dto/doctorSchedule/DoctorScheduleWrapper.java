package com.clinic.appointment.dto.doctorSchedule;

import lombok.Data;

import java.util.List;

@Data
public class DoctorScheduleWrapper {
    private List<DoctorScheduleCreateDTO> schedules;
}
