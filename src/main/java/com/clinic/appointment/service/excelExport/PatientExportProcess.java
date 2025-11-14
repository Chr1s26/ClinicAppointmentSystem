//package com.clinic.appointment.service.excelExport;
//
//import com.clinic.appointment.dto.searchFilter.patient.PatientSearchQuery;
//import com.clinic.appointment.model.Patient;
//import com.clinic.appointment.model.constant.FileType;
//import com.clinic.appointment.service.ExportListingService;
//import com.clinic.appointment.service.FileService;
//import com.clinic.appointment.service.PatientSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PatientExportProcess extends CommonExportProcess<Patient, PatientSearchQuery> {
//
//    private final PatientSearchService patientSearchService;
//
//    public PatientExportProcess(ExportListingService exportListingService,
//                                FileService fileService,
//                                PatientSearchService patientSearchService) {
//        super(exportListingService, fileService);
//        this.patientSearchService = patientSearchService;
//    }
//
//    @Override
//    public String getRecordType() { return Patient.class.getSimpleName(); }
//
//    @Override
//    public FileType getDownloadFileType() { return FileType.Patient_Listing; }
//
//    @Override
//    public String getSheetName() { return "Patients"; }
//
//    @Override
//    public List<Patient> fetchData(PatientSearchQuery query) {
//        return patientSearchService.searchByQueryAll(query);
//    }
//
//    @Override
//    public List<ColumnSpec<Patient>> columns() {
//        return List.of(
//                new ColumnSpec<>("ID", p -> String.valueOf(p.getId()), null),
//                new ColumnSpec<>("Name", Patient::getName, null),
//                new ColumnSpec<>("Phone", Patient::getPhone, null),
//                new ColumnSpec<>("Email", Patient::getEmail, null),
//                new ColumnSpec<>("Gender", p -> p.getGenderType() != null ? p.getGenderType().name() : "", null),
//                new ColumnSpec<>("Patient Type", p -> p.getPatientType() != null ? p.getPatientType().name() : "", null),
//                new ColumnSpec<>("Status", Patient::getStatus, null)
//        );
//    }
//
//    @Override
//    public String getListingRoute() { return "/patients"; }
//}
