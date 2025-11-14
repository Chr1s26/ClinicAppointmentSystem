package com.clinic.appointment.service;

import com.clinic.appointment.dto.searchFilter.appUser.AppUserSearchQuery;
import com.clinic.appointment.entity.specification.AppUserSpecification;
import com.clinic.appointment.dto.appUser.AppUserDTO;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserSearchService {

    private final CommonSearchService commonSearchService;
    private final AppUserRepository appUserRepository;
    private final ModelMapper mapper;

    public Page<AppUserDTO> searchUsers(AppUserSearchQuery query) {
        Page<AppUser> page = commonSearchService.searchByQuery(
                appUserRepository,
                AppUserSpecification::fromFilter,
                query
        );
        return page.map(a -> mapper.map(a, AppUserDTO.class));
    }

    public List<AppUser> searchUsersAll(AppUserSearchQuery query) {
        return commonSearchService.searchByQueryAll(
                appUserRepository,
                AppUserSpecification::fromFilter,
                query
        );
    }

}
