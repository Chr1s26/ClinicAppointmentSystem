package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.appointmentSlot.AppointmentSlotSearchQuery;
import com.clinic.appointment.model.AppointmentSlot;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.*;
import com.clinic.appointment.service.AppointmentSlotSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AppointmentSlotExportProcess
        extends CommonExportProcess<AppointmentSlot, AppointmentSlotSearchQuery> {

    private final AppointmentSlotSearchService searchService;

    public AppointmentSlotExportProcess(
            ExportListingService exportListingService,
            FileService fileService,
            AppointmentSlotSearchService searchService) {
        super(exportListingService, fileService);
        this.searchService = searchService;
    }

    @Override
    public String getRecordType() { return "AppointmentSlot"; }

    @Override
    public FileType getDownloadFileType() { return FileType.AppointmentSlot_Listing; }

    @Override
    public String getSheetName() { return "Appointment Slots"; }

    @Override
    public List<AppointmentSlot> fetchData(AppointmentSlotSearchQuery query) {
        return searchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<AppointmentSlot>> columns() {
        return List.of(
                new ColumnSpec<>("ID", s -> String.valueOf(s.getId()), null),
                new ColumnSpec<>("Doctor", s -> s.getDoctor().getName(), null),
                new ColumnSpec<>("Date", s -> s.getDate().toString(), null),
                new ColumnSpec<>("Time Slot", AppointmentSlot::getTimeSlot, null),
                new ColumnSpec<>("Booked", s -> s.isBooked() ? "YES" : "NO", null)
        );
    }

    @Override
    public String getListingRoute() { return "/appointment-slots"; }
}
