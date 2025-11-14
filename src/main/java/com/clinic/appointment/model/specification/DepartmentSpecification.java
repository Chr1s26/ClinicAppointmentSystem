package com.clinic.appointment.entity.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchFilter;
import com.clinic.appointment.model.Department;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    public static Specification<Department> fromFilter(DepartmentSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) {
            return null;
        }

        return switch (filter.getField()) {

            case NAME -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("departmentName"), filter.getMatchType(), filter.getValue());

            case DESCRIPTION -> (root, query, cb) ->
                    MatchType.toPredicate(cb, root.get("departmentDescription"), filter.getMatchType(), filter.getValue());

            case STATUS -> (root, query, cb) ->
                    cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
        };
    }
}
