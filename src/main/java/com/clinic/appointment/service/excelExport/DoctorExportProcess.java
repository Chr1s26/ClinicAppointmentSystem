package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.doctor.DoctorSearchQuery;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import com.clinic.appointment.service.search.DoctorSearchService;
import lombok.RequiredArgsConstructor;
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
                new ColumnSpec<>("Email", d -> d.getAppUser() != null ? d.getAppUser().getEmail() : "", null),
                new ColumnSpec<>("Departments", d -> String.join(",", d.getDepartments().stream().map(Department::getDepartmentName).toList()), null),
                new ColumnSpec<>("Status", Doctor::getStatus, null),
                new ColumnSpec<>("CreatedAt", d -> d.getCreatedAt() != null ? d.getCreatedAt().toString() : "", null)
        );
    }

    @Override
    public String getListingRoute() { return "/doctors"; }
}

