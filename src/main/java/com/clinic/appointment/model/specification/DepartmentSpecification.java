package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchFilter;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.constant.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    public static Specification<Department> fromFilter(DepartmentSearchFilter f) {

        if(f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case NAME -> stringSpec("departmentName", f);
            case DESCRIPTION -> stringSpec("departmentDescription", f);
            case STATUS -> (root, q, cb) -> {
                if(f.getValue() == null && f.getValue().isBlank())return null;
                StatusType statusType;
                try{
                    statusType = StatusType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("status"), statusType);
            };
        };
    }

    private static Specification<Department> stringSpec(String attr, DepartmentSearchFilter f){
        return (root, q, cb) -> {
            String v = f.getValue();
            if(v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()){
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
                case ENDS_WITH ->  cb.like(cb.lower(root.get(attr)), "%"+lv);
            };
        };
    }
}
