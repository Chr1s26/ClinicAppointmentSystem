package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.admin.AdminSearchQuery;
import com.clinic.appointment.model.Admin;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import com.clinic.appointment.service.search.AdminSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminExportProcess extends CommonExportProcess<Admin, AdminSearchQuery>{

    private final AdminSearchService adminSearchService;

    public AdminExportProcess(ExportListingService exportListingService, FileService fileService, AdminSearchService adminSearchService) {
        super(exportListingService, fileService);
        this.adminSearchService = adminSearchService;
    }

    @Override
    public String getRecordType() {
        return Admin.class.getSimpleName();
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.Admin_Listing;
    }

    @Override
    public String getSheetName() {
        return "Admin Listing";
    }

    @Override
    public List<Admin> fetchData(AdminSearchQuery query) {
        return adminSearchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Admin>> columns() {
        return List.of(
                new ColumnSpec<>("ID", a -> String.valueOf(a.getId()), null),
                new ColumnSpec<>("Name", Admin::getName, null),
                new ColumnSpec<>("Email", Admin::getEmail, null),
                new ColumnSpec<>("Phone", Admin::getPhone, null),
                new ColumnSpec<>("Date of Birth", a -> String.valueOf(a.getDateOfBirth()),null),
                new ColumnSpec<>("Gender",a -> a.getGenderType() != null ? a.getGenderType().toString() : "-", null),
                new ColumnSpec<>("Address", Admin::getAddress, null),
                new ColumnSpec<>("Status", a -> a.getStatus() != null ? a.getStatus().name() : "-", null),
                new ColumnSpec<>("Created At", u -> u.getCreatedAt() != null ? u.getCreatedAt().toString() : "-", null),
                new ColumnSpec<>("Updated At", u -> u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : "-", null),
                new ColumnSpec<>("Created By", u ->
                        (u.getCreatedBy() != null ? u.getCreatedBy().getUsername() : "-"), null),
                new ColumnSpec<>("Updated By", u ->
                        (u.getUpdatedBy() != null ? u.getUpdatedBy().getUsername() : "-"), null)
        );
    }

    @Override
    public String getListingRoute() {
        return "/admins";
    }
}
