package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.department.DepartmentSearchQuery;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import com.clinic.appointment.service.search.DepartmentSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentExportProcess extends CommonExportProcess<Department, DepartmentSearchQuery> {

    private final DepartmentSearchService departmentSearchService;

    public DepartmentExportProcess(ExportListingService exportListingService,
                                   FileService fileService,
                                   DepartmentSearchService departmentSearchService) {
        super(exportListingService, fileService);
        this.departmentSearchService = departmentSearchService;
    }

    @Override
    public String getRecordType() {
        return Department.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Department_Listing;
    }

    @Override
    public String getSheetName() {
        return "Departments";
    }

    @Override
    public List<Department> fetchData(DepartmentSearchQuery query) {
        return departmentSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Department>> columns() {
        return List.of(
                new ColumnSpec<>("ID", d -> d.getId().toString(), null),
                new ColumnSpec<>("Name", Department::getDepartmentName, null),
                new ColumnSpec<>("Description", Department::getDepartmentDescription, null),
                new ColumnSpec<>("Status", d -> d.getStatus() != null ? d.getStatus().name() : "", null),
                new ColumnSpec<>("Created at", d -> d.getCreatedAt() != null ? d.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", d -> d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", d -> d.getCreatedBy() != null ? d.getCreatedBy().getUsername() : "", null),
                new ColumnSpec<>("Updated by", d -> d.getUpdatedBy() != null ? d.getUpdatedBy().getUsername() : "", null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/departments";
    }
}
