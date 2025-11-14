//package com.clinic.appointment.entity.specification;
//
//import com.clinic.appointment.dto.searchFilter.MatchType;
//import com.clinic.appointment.dto.searchFilter.doctorSchedule.DoctorScheduleSearchFilter;
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
//            case DOCTOR_NAME -> (root, query, cb) -> {
//                Join<Object, Object> doctor = root.join("doctor");
//                return MatchType.toPredicate(cb,
//                        doctor.get("name"),
//                        filter.getMatchType(),
//                        filter.getValue());
//            };
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
//}
