package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchFilter;
import com.clinic.appointment.model.Doctor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public static Specification<Doctor> fromFilter(DoctorSearchFilter filter) {
        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {
            case NAME -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("name"), filter.getMatchType(), filter.getValue());
            case PHONE -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("phone"), filter.getMatchType(), filter.getValue());
            case EMAIL -> (root, query, cb) -> {
                Join<Object, Object> appUser = root.join("appUser");
                Path<String> p = appUser.get("email");
                return MatchType.toPredicate(cb, p, filter.getMatchType(), filter.getValue());
            };
            case STATUS -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
            case DEPARTMENT -> (root, query, cb) -> {
                Join<Object, Object> depts = root.join("departments");
                return cb.equal(cb.lower(depts.get("departmentName")), filter.getValue().toLowerCase());
            };
            case GENDER -> (root, query, cb) ->
                    cb.equal(root.get("genderType"), filter.getValue());
        };
    }
}
