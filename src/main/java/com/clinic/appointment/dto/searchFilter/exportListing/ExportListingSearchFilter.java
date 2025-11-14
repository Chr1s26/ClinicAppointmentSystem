package com.clinic.appointment.dto.searchFilter.exportListing;


import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportListingSearchFilter {
    private ExportListingSearchField field;
    private MatchType matchType;
    private String value;
}
