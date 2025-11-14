package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.exportListing.ExportListingSearchQuery;
import com.clinic.appointment.model.ExportListing;
import com.clinic.appointment.model.specification.ExportListingSpecification;
import com.clinic.appointment.repository.ExportListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExportListingSearchService {

    private final CommonSearchService commonSearchService;
    private final ExportListingRepository exportListingRepository;

    public Page<ExportListing> searchByQuery(ExportListingSearchQuery query) {
        return commonSearchService.searchByQuery(exportListingRepository, ExportListingSpecification::fromFilter, query);
    }

    public List<ExportListing> searchByQueryAll(ExportListingSearchQuery query) {
        return commonSearchService.searchByQueryAll(exportListingRepository, ExportListingSpecification::fromFilter,query);
    }
}
