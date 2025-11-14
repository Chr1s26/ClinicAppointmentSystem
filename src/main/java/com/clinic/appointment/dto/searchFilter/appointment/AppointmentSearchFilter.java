package com.clinic.appointment.dto.searchFilter.appointment;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchFilter {
    private AppointmentSearchField field;
    private MatchType matchType;
    private String value;
}
