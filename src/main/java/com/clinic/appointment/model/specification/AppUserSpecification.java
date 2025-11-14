package com.clinic.appointment.entity.specification;

import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.appUser.AppUserSearchFilter;
import com.clinic.appointment.model.AppUser;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AppUserSpecification {

    public static Specification<AppUser> fromFilter(AppUserSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank())
            return null;

        return switch (filter.getField()) {

            case USERNAME ->
                    (root, query, cb) -> MatchType.toPredicate(cb, root.get("username"),
                            filter.getMatchType(), filter.getValue());

            case EMAIL ->
                    (root, query, cb) -> MatchType.toPredicate(cb, root.get("email"),
                            filter.getMatchType(), filter.getValue());

            case ROLE -> (root, query, cb) -> {
                Join<Object, Object> roles = root.join("roles");
                return MatchType.toPredicate(cb, roles.get("roleName"),
                        filter.getMatchType(), filter.getValue());
            };

            case STATUS ->
                    (root, query, cb) -> cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
        };
    }
}
