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
                new ColumnSpec<>("Status", d -> String.valueOf(d.getStatus()), null),
                new ColumnSpec<>("Created At", d -> String.valueOf(d.getCreatedAt()), null),
                new ColumnSpec<>("Updated At", d -> String.valueOf(d.getUpdatedAt()), null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/departments";
    }
}
