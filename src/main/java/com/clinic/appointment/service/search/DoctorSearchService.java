package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchQuery;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.service.search.CommonSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorSearchService {

    private final CommonSearchService commonSearchService;
    private final DoctorRepository doctorRepository;

    public Page<Doctor> searchByQuery(DoctorSearchQuery query) {
        return commonSearchService.searchByQuery(doctorRepository, com.clinic.appointment.entity.specification.DoctorSpecification::fromFilter, query);
    }

    public List<Doctor> searchByQueryAll(DoctorSearchQuery query) {
        return commonSearchService.searchByQueryAll(doctorRepository, com.clinic.appointment.entity.specification.DoctorSpecification::fromFilter, query);
    }
}
