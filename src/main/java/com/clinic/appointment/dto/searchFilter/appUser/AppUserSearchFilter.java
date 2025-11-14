package com.clinic.appointment.dto.searchFilter.appUser;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserSearchFilter {
    private AppUserSearchField field;
    private MatchType matchType;
    private String value;
}
