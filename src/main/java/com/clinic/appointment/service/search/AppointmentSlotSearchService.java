//package com.clinic.appointment.service;
//
//import com.clinic.appointment.dto.searchFilter.appointmentSlot.AppointmentSlotSearchQuery;
//import com.clinic.appointment.entity.specification.AppointmentSlotSpecification;
//import com.clinic.appointment.model.AppointmentSlot;
//import com.clinic.appointment.repository.AppointmentSlotRepository;
//import com.clinic.appointment.service.search.CommonSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AppointmentSlotSearchService {
//
//    private final CommonSearchService commonSearchService;
//    private final AppointmentSlotRepository repository;
//
//    public Page<AppointmentSlot> searchByQuery(AppointmentSlotSearchQuery query) {
//        return commonSearchService.searchByQuery(
//                repository,
//                AppointmentSlotSpecification::fromFilter,
//                query
//        );
//    }
//
//    public List<AppointmentSlot> searchByQueryAll(AppointmentSlotSearchQuery query) {
//        return commonSearchService.searchByQueryAll(
//                repository,
//                AppointmentSlotSpecification::fromFilter,
//                query
//        );
//    }
//}
