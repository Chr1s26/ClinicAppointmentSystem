package com.clinic.appointment.dto.searchFilter.patient;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientSearchFilter {
    private PatientSearchField field;
    private MatchType matchType;
    private String value;
}
