package com.clinic.appointment.service.search;

import com.clinic.appointment.dto.searchFilter.SearchQuery;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class CommonSearchService {

    public <E, F> Page<E> searchByQuery(
            JpaSpecificationExecutor<E> repository,
            Function<F, Specification<E>> specFunction,
            SearchQuery<F> query
    ) {
        int page = (query.getPageNumber() == null || query.getPageNumber() < 0) ? 0 : query.getPageNumber();
        int size = (query.getPageSize() == null || query.getPageSize() < 1) ? 10 : query.getPageSize();
        String sortBy = (query.getSortBy() == null || query.getSortBy().isBlank()) ? "createdAt" : query.getSortBy();
        Sort.Direction dir = (query.getSortDirection() == null || query.getSortDirection() == SortDirection.DESC)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Specification<E> spec = Specification.where(null);
        if (query.getFilterList() != null) {
            for (F filter : query.getFilterList()) {
                Specification<E> s = specFunction.apply(filter);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }

        return repository.findAll(spec, pageable);
    }

    public <E, F> List<E> searchByQueryAll(
            JpaSpecificationExecutor<E> repository,
            Function<F, Specification<E>> specFunction,
            SearchQuery<F> query
    ) {
        Specification<E> spec = buildSpecification(query, specFunction);
        return repository.findAll(spec);
    }

    private <E, F> Specification<E> buildSpecification(
            SearchQuery<F> query,
            Function<F, Specification<E>> specFunction
    ) {
        Specification<E> spec = Specification.where(null);

        if (query.getFilterList() != null) {
            for (F filter : query.getFilterList()) {
                Specification<E> s = specFunction.apply(filter);
                if (s != null) spec = (spec == null) ? Specification.where(s) : spec.and(s);
            }
        }

        return spec;
    }
}
