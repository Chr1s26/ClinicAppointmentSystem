package com.clinic.appointment.entity.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.patient.PatientSearchFilter;
import com.clinic.appointment.model.Patient;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecification {

    public static Specification<Patient> fromFilter(PatientSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {
            case NAME -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("name"), filter.getMatchType(), filter.getValue());

            case PHONE -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("phone"), filter.getMatchType(), filter.getValue());

            case EMAIL -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("email"), filter.getMatchType(), filter.getValue());

            case GENDER -> (root, query, cb) ->
                    cb.equal(root.get("genderType"), filter.getValue());

            case PATIENT_TYPE -> (root, query, cb) ->
                    cb.equal(root.get("patientType"), filter.getValue());

            case STATUS -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
        };
    }
}
