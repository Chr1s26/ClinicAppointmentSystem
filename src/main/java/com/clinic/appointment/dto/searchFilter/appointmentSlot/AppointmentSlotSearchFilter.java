package com.clinic.appointment.dto.searchFilter.appointmentSlot;

import com.clinic.appointment.dto.searchFilter.MatchType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotSearchFilter {
    private AppointmentSlotSearchField field;
    private MatchType matchType;
    private String value;
}
