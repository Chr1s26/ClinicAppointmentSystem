package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.appointment.AppointmentSearchQuery;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentExportProcess extends CommonExportProcess<Appointment, AppointmentSearchQuery> {

    private final AppointmentSearchService searchService;

    public AppointmentExportProcess(ExportListingService exportListingService, FileService fileService, AppointmentSearchService searchService) {
        super(exportListingService, fileService);
        this.searchService = searchService;
    }

    @Override
    public String getRecordType() { return "Appointment"; }

    @Override
    public FileType getDownloadFileType() { return FileType.Appointment_Listing; }

    @Override
    public String getSheetName() { return "Appointments"; }

    @Override
    public List<Appointment> fetchData(AppointmentSearchQuery query) {
        return searchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<Appointment>> columns() {
        return List.of(
                new ColumnSpec<>("ID", a -> String.valueOf(a.getId()), null),
                new ColumnSpec<>("Patient", a -> a.getPatient() != null ? a.getPatient().getName() : "", null),
                new ColumnSpec<>("Doctor", a -> a.getDoctor() != null ? a.getDoctor().getName() : "", null),
                new ColumnSpec<>("Department", a -> a.getDepartment() != null ? a.getDepartment().getDepartmentName() : "", null),
                new ColumnSpec<>("Date", a -> a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : "", null),
                new ColumnSpec<>("Time", Appointment::getTimeSlot, null),
                new ColumnSpec<>("Status", a -> a.getAppointmentStatus() != null ? a.getAppointmentStatus().name() : "", null)
        );
    }

    @Override
    public String getListingRoute() { return "/appointments"; }
}
