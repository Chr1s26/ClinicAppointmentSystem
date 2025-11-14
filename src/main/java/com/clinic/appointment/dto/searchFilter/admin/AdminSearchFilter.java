package com.clinic.appointment.dto.searchFilter.admin;

import com.clinic.appointment.dto.searchFilter.MatchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSearchFilter {
    private AdminSearchField field;
    private MatchType matchType;
    private String value;
}
