package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchQuery;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.specification.DepartmentSpecification;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentSearchService {

    private final CommonSearchService commonSearchService;
    private final DepartmentRepository departmentRepository;

    public Page<Department> searchByQuery(DepartmentSearchQuery query) {
        return commonSearchService.searchByQuery(departmentRepository, DepartmentSpecification::fromFilter, query);
    }

    public List<Department> searchByQueryAll(DepartmentSearchQuery query) {
        return commonSearchService.searchByQueryAll(departmentRepository, DepartmentSpecification::fromFilter, query);
    }
}

