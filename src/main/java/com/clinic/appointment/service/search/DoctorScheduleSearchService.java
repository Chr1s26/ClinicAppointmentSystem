//package com.clinic.appointment.service;
//
//import com.clinic.appointment.dto.searchFilter.doctorSchedule.DoctorScheduleSearchQuery;
//import com.clinic.appointment.entity.specification.DoctorScheduleSpecification;
//import com.clinic.appointment.model.DoctorSchedule;
//import com.clinic.appointment.repository.DoctorScheduleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class DoctorScheduleSearchService {
//
//    private final CommonSearchService commonSearchService;
//    private final DoctorScheduleRepository doctorScheduleRepository;
//
//    public Page<DoctorSchedule> searchByQuery(DoctorScheduleSearchQuery query) {
//        return commonSearchService.searchByQuery(
//                doctorScheduleRepository,
//                DoctorScheduleSpecification::fromFilter,
//                query
//        );
//    }
//
//    public List<DoctorSchedule> searchByQueryAll(DoctorScheduleSearchQuery query) {
//        return commonSearchService.searchByQueryAll(
//                doctorScheduleRepository,
//                DoctorScheduleSpecification::fromFilter,
//                query
//        );
//    }
//}
