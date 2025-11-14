package com.clinic.appointment.service;

import com.clinic.appointment.dto.searchFilter.appUser.AppUserSearchQuery;
import com.clinic.appointment.dto.appUser.AppUserDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.specification.AppUserSpecification;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.service.search.CommonSearchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserSearchService {

    private final CommonSearchService commonSearchService;
    private final AppUserRepository appUserRepository;
    private final ModelMapper mapper;

    public Page<AppUser> searchByQuery(AppUserSearchQuery query) {
        return commonSearchService.searchByQuery(appUserRepository, AppUserSpecification::fromFilter ,query);
    }

    public List<AppUser> searchByQueryAll(AppUserSearchQuery query) {
        return commonSearchService.searchByQueryAll(appUserRepository, AppUserSpecification::fromFilter, query);
    }

}
