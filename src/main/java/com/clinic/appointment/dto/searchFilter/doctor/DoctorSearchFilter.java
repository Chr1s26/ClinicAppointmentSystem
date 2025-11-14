package com.clinic.appointment.dto.searchFilter.doctor;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchFilter {
    private DoctorSearchField field;
    private MatchType matchType;
    private String value;
}
