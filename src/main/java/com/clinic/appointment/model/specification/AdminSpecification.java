package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.admin.AdminSearchFilter;
import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.constant.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecification {
    
    public static Specification<Admin> fromFilter(AdminSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {
            case NAME -> stringSpec("name",filter);
            case PHONE -> stringSpec("phone",filter);
            case EMAIL -> stringSpec("email",filter);
            case STATUS -> statusSpec(filter);
        };
    }

    private static Specification<Admin> stringSpec(String attr, AdminSearchFilter f){
        return (root, q, cb) -> {
            String v = f.getValue();
            if(v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%"+lv);
            };
        };
    }
    
    private static Specification<Admin> statusSpec(AdminSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                StatusType status = StatusType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
