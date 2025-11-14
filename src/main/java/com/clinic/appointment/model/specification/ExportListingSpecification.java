package com.clinic.appointment.model.specification;


import com.clinic.appointment.dto.searchFilter.exportListing.ExportListingSearchFilter;
import com.clinic.appointment.model.ExportListing;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.StatusType;
import org.springframework.data.jpa.domain.Specification;


public class ExportListingSpecification {

    public static Specification<ExportListing> fromFilter(ExportListingSearchFilter f) {
        if (f == null || f.getField() == null) return null;

        return switch (f.getField()) {
            case FILE_NAME -> stringSpec("fileName", f);
            case FILE_TYPE -> fileTypeSpec(f);
            case STATUS -> statusSpec(f);
        };
    }

    private static Specification<ExportListing> stringSpec(String attr, ExportListingSearchFilter f) {
        return (root, q, cb) -> {
            String v = f.getValue();
            if (v == null || v.isBlank()) return null;
            String lv = v.toLowerCase();
            return switch (f.getMatchType()) {
                case EXACT -> cb.equal(cb.lower(root.get(attr)), lv);
                case CONTAINS -> cb.like(cb.lower(root.get(attr)), "%" + lv + "%");
                case START_WITH -> cb.like(cb.lower(root.get(attr)), lv + "%");
                case ENDS_WITH -> cb.like(cb.lower(root.get(attr)), "%" + lv);
            };
        };
    }

    private static Specification<ExportListing> statusSpec(ExportListingSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                StatusType status = StatusType.valueOf(f.getValue().toUpperCase());
                return cb.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    private static Specification<ExportListing> fileTypeSpec(ExportListingSearchFilter f) {
        return (root, q, cb) -> {
            if (f.getValue() == null || f.getValue().isBlank()) return null;
            try {
                FileType type = FileType.valueOf(f.getValue());
                return cb.equal(root.get("fileType"), type);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}

