package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchFilter;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.StatusType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DoctorSpecification {

    public static Specification<Doctor> fromFilter(DoctorSearchFilter f) {
        if (f.getValue() == null || f.getValue().isBlank()) return null;

        return switch (f.getField()) {
            case NAME -> stringSpec("name", f);
            case PHONE -> stringSpec("phone", f);
            case DATE_OF_BIRTH -> (root, q, cb) -> {
                if(f.getValue() == null || f.getValue().isBlank()) return null;
                String dobString;
                try{
                    dobString = f.getValue().trim();
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("dateOfBirth"), LocalDate.parse(dobString));
            };
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
            case GENDER -> (root, q, cb) -> {
                if(f.getValue() == null && !f.getValue().isBlank()) return null;
                GenderType genderType;
                try{
                    genderType = GenderType.valueOf(f.getValue());
                }catch (Exception e){
                    return null;
                }
                return cb.equal(root.get("genderType"), genderType);
            };
        };
    }

    private static Specification<Doctor> stringSpec(String attr, DoctorSearchFilter f){
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
