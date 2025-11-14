package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.patient.PatientSearchFilter;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.model.constant.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecification {

    public static Specification<Patient> fromFilter(PatientSearchFilter filter) {

        if (filter.getValue() == null || filter.getValue().isBlank()) return null;

        return switch (filter.getField()) {
            case NAME -> stringSpec("name",filter);
            case PHONE -> stringSpec("phone",filter);
            case EMAIL -> stringSpec("email",filter);
            case GENDER -> genderSpec(filter);
            case PATIENT_TYPE -> patientTypeSpec(filter);
            case STATUS -> statusSpec(filter);
        };
    }

    private static Specification<Patient> stringSpec(String attr, PatientSearchFilter f){
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

    private static Specification<Patient> statusSpec(PatientSearchFilter f) {
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

    private static Specification<Patient> patientTypeSpec(PatientSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                PatientType patientType = PatientType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("patientType"), patientType);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<Patient> genderSpec(PatientSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                GenderType genderType = GenderType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("genderType"), genderType);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
