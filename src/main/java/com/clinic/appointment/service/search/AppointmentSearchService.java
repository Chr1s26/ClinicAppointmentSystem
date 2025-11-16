package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchQuery;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.specification.AppointmentSpecification;
import com.clinic.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentSearchService {

    private final CommonSearchService commonSearchService;
    private final AppointmentRepository appointmentRepository;

    public Page<Appointment> searchByQuery(AppointmentSearchQuery query) {
        return commonSearchService.searchByQuery(appointmentRepository, AppointmentSpecification::fromFilter, query);
    }

    public List<Appointment> searchByQueryAll(AppointmentSearchQuery query) {
        return commonSearchService.searchByQueryAll(appointmentRepository, AppointmentSpecification::fromFilter, query);
    }
}
