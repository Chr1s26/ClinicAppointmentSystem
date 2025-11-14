package com.clinic.appointment.dto.searchFilter.doctorSchedule;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleSearchFilter {
    private DoctorScheduleSearchField field;
    private MatchType matchType;
    private String value;
}
