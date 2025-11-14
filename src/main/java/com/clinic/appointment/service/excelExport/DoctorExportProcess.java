package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchQuery;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import com.clinic.appointment.service.search.DoctorSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorExportProcess extends CommonExportProcess<Doctor, DoctorSearchQuery> {

    private final DoctorSearchService doctorSearchService;

    public DoctorExportProcess(ExportListingService exportListingService, FileService fileService, DoctorSearchService doctorSearchService) {
        super(exportListingService, fileService);
        this.doctorSearchService = doctorSearchService;
    }

    @Override
    public String getRecordType() { return Doctor.class.getSimpleName(); }

    @Override
    public FileType getDownloadFileType() { return FileType.Doctor_Listing; }

    @Override
    public String getSheetName() { return "Doctors"; }

    @Override
    public List<Doctor> fetchData(DoctorSearchQuery query) { return doctorSearchService.searchByQueryAll(query); }

    @Override
    public List<ColumnSpec<Doctor>> columns() {
        return List.of(
                new ColumnSpec<>("ID", d -> String.valueOf(d.getId()), null),
                new ColumnSpec<>("Name", Doctor::getName, null),
                new ColumnSpec<>("Phone", Doctor::getPhone, null),
                new ColumnSpec<>("Email", Doctor::getEmail, null),
                new ColumnSpec<>("Address", Doctor::getAddress, null),
                new ColumnSpec<>("Date of Birth", d -> String.valueOf(d.getDateOfBirth()),null),
                new ColumnSpec<>("Gender Type", d -> d.getGenderType() != null ? d.getGenderType().name() : "", null),
                new ColumnSpec<>("Status", d -> d.getStatus() != null ? d.getStatus().name() : "", null),
                new ColumnSpec<>("CreatedAt", d -> d.getCreatedAt() != null ? d.getCreatedAt().toString() : "", null),
                new ColumnSpec<>("Updated at", d -> d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : "", null),
                new ColumnSpec<>("Created by", d -> d.getCreatedBy() != null ? d.getCreatedBy().getUsername() : "", null),
                new ColumnSpec<>("Updated by", d -> d.getUpdatedBy() != null ? d.getUpdatedBy().getUsername() : "", null)
        );
    }

    @Override
    public String getListingRoute() { return "/doctors"; }
}

