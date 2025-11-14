package com.clinic.appointment.entity.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.appointmentSlot.AppointmentSlotSearchFilter;
import com.clinic.appointment.model.AppointmentSlot;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSlotSpecification {

    public static Specification<AppointmentSlot> fromFilter(AppointmentSlotSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {

            case DOCTOR_NAME -> (root, query, cb) -> {
                Join<Object, Object> doctor = root.join("doctor");
                return MatchType.toPredicate(cb,
                        doctor.get("name"),
                        filter.getMatchType(),
                        filter.getValue());
            };

            case DATE -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("date"), filter.getMatchType(), filter.getValue());

            case TIME_SLOT -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("timeSlot"), filter.getMatchType(), filter.getValue());

            case BOOKED -> (root, query, cb) ->
                    cb.equal(root.get("booked"), Boolean.valueOf(filter.getValue()));

            case STATUS -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
        };
    }
}
