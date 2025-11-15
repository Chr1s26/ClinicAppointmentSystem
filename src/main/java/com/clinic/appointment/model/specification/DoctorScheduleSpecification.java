//package com.clinic.appointment.model.specification;
//
//import com.clinic.appointment.dto.searchFilter.MatchType;
//import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchFilter;
//import com.clinic.appointment.dto.searchFilter.doctorSchedule.DoctorScheduleSearchFilter;
//import com.clinic.appointment.model.Doctor;
//import com.clinic.appointment.model.DoctorSchedule;
//import jakarta.persistence.criteria.Join;
//import org.springframework.data.jpa.domain.Specification;
//
//public class DoctorScheduleSpecification {
//
//    public static Specification<DoctorSchedule> fromFilter(DoctorScheduleSearchFilter filter) {
//
//        if (filter.getValue() == null || filter.getValue().isBlank()) return null;
//
//        return switch (filter.getField()) {
//
//            case DOCTOR_NAME -> stringSpec("name",filter),
//
//            case DAY_OF_WEEK -> (root, query, cb) ->
//                    MatchType.toPredicate(cb, root.get("dayOfWeek"), filter.getMatchType(), filter.getValue());
//
//            case STATUS -> (root, query, cb) ->
//                    cb.equal(cb.lower(root.get("status")), filter.getValue().toLowerCase());
//
//            case AVAILABLE -> (root, query, cb) ->
//                    cb.equal(root.get("available"), Boolean.valueOf(filter.getValue()));
//        };
//    }
//    private static Specification<DoctorSchedule> stringSpec(String attr, DoctorScheduleSearchFilter f){
//        return (root, q, cb) -> {
//            String v = f.getValue();
//            if(v == null || v.isBlank()) return null;
//            String lv = v.toLowerCase();
//            return switch (f.getMatchType()){
//                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
//                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%"+lv+"%");
//                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv+"%");
//                case ENDS_WITH ->  cb.like(cb.lower(root.get(attr)), "%"+lv);
//            };
//        };
//    }
//}
