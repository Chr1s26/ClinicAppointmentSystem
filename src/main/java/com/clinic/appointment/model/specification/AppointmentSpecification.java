package com.clinic.appointment.entity.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchFilter;
import com.clinic.appointment.model.Appointment;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecification {

    public static Specification<Appointment> fromFilter(AppointmentSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {
            case PATIENT_NAME -> (root, query, cb) -> {
                Join<Object, Object> patient = root.join("patient");
                return MatchType.toPredicate(cb, patient.get("name"), filter.getMatchType(), filter.getValue());
            };

            case DOCTOR_NAME -> (root, query, cb) -> {
                Join<Object, Object> doctor = root.join("doctor");
                return MatchType.toPredicate(cb, doctor.get("name"), filter.getMatchType(), filter.getValue());
            };

            case DATE -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("appointmentDate"), filter.getMatchType(), filter.getValue());

            case TIME_SLOT -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("timeSlot"), filter.getMatchType(), filter.getValue());

            case STATUS -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("appointmentStatus")), filter.getValue().toLowerCase());

            case DEPARTMENT -> (root, query, cb) -> {
                Join<Object, Object> dept = root.join("department");
                return MatchType.toPredicate(cb, dept.get("departmentName"), filter.getMatchType(), filter.getValue());
            };
        };
    }
}
