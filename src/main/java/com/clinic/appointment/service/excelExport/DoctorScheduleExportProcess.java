package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.dto.searchFilter.doctorSchedule.DoctorScheduleSearchQuery;
import com.clinic.appointment.model.DoctorSchedule;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.*;
import com.clinic.appointment.service.DoctorScheduleSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorScheduleExportProcess
        extends CommonExportProcess<DoctorSchedule, DoctorScheduleSearchQuery> {

    private final DoctorScheduleSearchService searchService;

    public DoctorScheduleExportProcess(
            ExportListingService exportListingService,
            FileService fileService,
            DoctorScheduleSearchService searchService
    ) {
        super(exportListingService, fileService);
        this.searchService = searchService;
    }

    @Override
    public String getRecordType() { return "DoctorSchedule"; }

    @Override
    public FileType getDownloadFileType() { return FileType.DoctorSchedule_Listing; }

    @Override
    public String getSheetName() { return "Doctor Schedules"; }

    @Override
    public List<DoctorSchedule> fetchData(DoctorScheduleSearchQuery query) {
        return searchService.searchByQueryAll(query);
    }

    @Override
    public List<ColumnSpec<DoctorSchedule>> columns() {
        return List.of(
                new ColumnSpec<>("ID", s -> String.valueOf(s.getId()), null),
                new ColumnSpec<>("Doctor", s -> s.getDoctor().getName(), null),
                new ColumnSpec<>("Day", DoctorSchedule::getDayOfWeek, null),
                new ColumnSpec<>("Start Time", DoctorSchedule::getStartTime, null),
                new ColumnSpec<>("End Time", DoctorSchedule::getEndTime, null),
                new ColumnSpec<>("Available", s -> String.valueOf(s.isAvailable()), null)
        );
    }

    @Override
    public String getListingRoute() { return "/doctor-schedule"; }
}
