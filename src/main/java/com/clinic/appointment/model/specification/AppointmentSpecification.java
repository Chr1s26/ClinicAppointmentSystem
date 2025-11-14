package com.clinic.appointment.model.specification;

import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchFilter;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.constant.AppointmentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AppointmentSpecification {

    public static Specification<Appointment> fromFilter(AppointmentSearchFilter f) {

        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {

            case PATIENT_NAME -> patientName(f.getValue());

            case DOCTOR_NAME -> doctorName(f.getValue());

            case DEPARTMENT -> departmentName(f.getValue());

            case TIME_SLOT -> like("timeSlot", f.getValue());

            case DATE -> dateFilter(f);

            case STATUS -> statusFilter(f);
        };
    }

    private static Specification<Appointment> doctorName(String name) {
        return (root, q, cb) ->
                cb.like(cb.lower(root.get("doctor").get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    private static Specification<Appointment> patientName(String name) {
        return (root, q, cb) ->
                cb.like(cb.lower(root.get("patient").get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    private static Specification<Appointment> departmentName(String name) {
        return (root, q, cb) ->
                cb.like(cb.lower(root.get("department").get("departmentName")),
                        "%" + name.toLowerCase() + "%");
    }

    private static Specification<Appointment> like(String field, String value) {
        return (root, q, cb) -> (value == null || value.isBlank())
                ? null
                : cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%");
    }

    private static Specification<Appointment> dateFilter(AppointmentSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;

            try {
                LocalDate date = LocalDate.parse(f.getValue().trim());
                return cb.equal(root.get("appointmentDate"), date);
            } catch (Exception e) {
                return null;
            }
        };
    }

    private static Specification<Appointment> statusFilter(AppointmentSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;

            try {
                AppointmentStatus status = AppointmentStatus.valueOf(f.getValue().trim().toUpperCase());
                return cb.equal(root.get("appointmentStatus"), status);
            } catch (Exception e) {
                return null;
            }
        };
    }
}
