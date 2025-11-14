package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.patient.PatientSearchQuery;
import com.clinic.appointment.model.specification.PatientSpecification;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.PatientRepository;
import com.clinic.appointment.service.search.CommonSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientSearchService {

    private final CommonSearchService commonSearchService;
    private final PatientRepository patientRepository;

    public Page<Patient> searchByQuery(PatientSearchQuery query) {
        return commonSearchService.searchByQuery(patientRepository, PatientSpecification::fromFilter, query);
    }

    public List<Patient> searchByQueryAll(PatientSearchQuery query) {
        return commonSearchService.searchByQueryAll(patientRepository, PatientSpecification::fromFilter, query);
    }
}
