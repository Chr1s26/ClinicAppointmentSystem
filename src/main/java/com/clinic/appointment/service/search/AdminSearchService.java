package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.admin.AdminSearchQuery;
import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.specification.AdminSpecification;
import com.clinic.appointment.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminSearchService {

    private final CommonSearchService commonSearchService;
    private final AdminRepository adminRepository;

    public Page<Admin> searchByQuery(AdminSearchQuery query) {
        return commonSearchService.searchByQuery(adminRepository, AdminSpecification::fromFilter, query);
    }

    public List<Admin> searchByQueryAll(AdminSearchQuery query) {
        return commonSearchService.searchByQueryAll(adminRepository, AdminSpecification::fromFilter, query);
    }
}
