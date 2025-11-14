package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.appUser.AppUserSearchQuery;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.AppUserSearchService;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserExportProcess extends CommonExportProcess<AppUser, AppUserSearchQuery> {

    private final AppUserSearchService searchService;

    public AppUserExportProcess(ExportListingService exportListingService,
                                FileService fileService,
                                AppUserSearchService searchService) {
        super(exportListingService, fileService);
        this.searchService = searchService;
    }

    @Override
    public String getRecordType() {
        return AppUser.class.getSimpleName(); // "AppUser"
    }

    @Override
    public FileType getDownloadFileType() {
        return FileType.AppUser_Listing;
    }

    @Override
    public String getSheetName() {
        return "App Users";
    }

    @Override
    public List<AppUser> fetchData(AppUserSearchQuery query) {
        return searchService.searchUsersAll(query); // You will create this method below
    }

    @Override
    public List<ColumnSpec<AppUser>> columns() {
        return List.of(
                new ColumnSpec<>("ID", u -> String.valueOf(u.getId()), null),
                new ColumnSpec<>("Username", AppUser::getUsername, null),
                new ColumnSpec<>("Email", AppUser::getEmail, null),
                new ColumnSpec<>("Roles", u -> u.getRoles() != null
                        ? String.join(", ", u.getRoles().stream().map(r -> r.getRoleName()).toList())
                        : "", null),
                new ColumnSpec<>("Confirmed", u -> u.isAccountConfirmed() ? "YES" : "NO", null),
                new ColumnSpec<>("Status", u -> u.getStatus() != null ? u.getStatus() : "-", null),
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
        return "/users";
    }
}
