package com.clinic.appointment.dto.searchFilter.department;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentSearchFilter {
    private DepartmentSearchField field;
    private MatchType matchType;
    private String value;
}
