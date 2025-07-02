package com.clinic.appointment.test.service;

import com.clinic.appointment.dto.DoctorDTO;
import com.clinic.appointment.dto.DoctorResponse;
import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.GenderType;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.service.DoctorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    public void doctorService_CreateDoctor_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor");
        doctor.setAddress("Tonson Apartment");
        doctor.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctor.setPhone("097874634");
        doctor.setGenderType(GenderType.MALE);

        when(doctorRepository.findDoctorByNameIgnoreCase(doctor.getName())).thenReturn(Optional.empty());
        when(doctorRepository.findByPhone(doctor.getPhone())).thenReturn(Optional.empty());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorService.createDoctor(doctor,null);

        Assertions.assertThat(savedDoctor).isNotNull();
        Assertions.assertThat(savedDoctor.getName()).isEqualTo("Doctor");
    }

    @Test
    void testCreateDoctor_ValidationFails() {
        Doctor invalidDoctor = new Doctor(); // missing required fields

        assertThrows(NullPointerException.class, () -> doctorService.createDoctor(invalidDoctor, null));
    }

    @Test
    public void doctorService_UpdateDoctor_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Doctor 1");
        doctor.setAddress("Tonson Apartment");
        doctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor.setPhone("097874634");
        doctor.setGenderType(GenderType.MALE);

        Doctor existing = new Doctor();
        existing.setId(1L);
        existing.setName("Doctor");
        existing.setAddress("Tonson Apartment");
        existing.setDateOfBirth(LocalDate.of(1990, 01, 01));
        existing.setPhone("097874634");
        existing.setGenderType(GenderType.MALE);

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setName("Doctor 1");
        doctorDTO.setAddress("Tonson Apartment");
        doctorDTO.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctorDTO.setPhone("097874634");
        doctorDTO.setGenderType(GenderType.MALE);

        Doctor updateddoctor = new Doctor();
        updateddoctor.setId(1L);
        updateddoctor.setName("Doctor 1");
        updateddoctor.setAddress("Tonson Apartment");
        updateddoctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        updateddoctor.setPhone("097874634");
        updateddoctor.setGenderType(GenderType.MALE);

        when(doctorRepository.findDoctorByName(doctor.getId(), doctor.getName())).thenReturn(Optional.empty());
        when(doctorRepository.findDoctorByPhone(doctor.getId(),doctor.getPhone())).thenReturn(Optional.empty());
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorRepository.save(existing)).thenReturn(updateddoctor);

        Doctor doctor1 = doctorService.updateDoctor(1L,doctorDTO,null);

        assertNotNull(doctor1);
        assertEquals("Doctor 1", doctor1.getName());
    }

    @Test
    public void testFindDoctorById_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Doctor 1");
        doctor.setAddress("Tonson Apartment");
        doctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor.setPhone("097874634");
        doctor.setGenderType(GenderType.MALE);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Doctor result =  doctorService.findDoctorById(1L);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void testGetAllDoctorsPagination(){
        Sort sortByAndOrder = Sort.by(Sort.Direction.ASC, "id");

        Pageable pageable = PageRequest.of(0, 10, sortByAndOrder);

        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setName("Doctor 1");
        doctorDTO1.setAddress("Tonson Apartment");
        doctorDTO1.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctorDTO1.setPhone("097874634");
        doctorDTO1.setGenderType(GenderType.MALE);

        DoctorDTO doctorDTO2 = new DoctorDTO();
        doctorDTO2.setName("Doctor 1");
        doctorDTO2.setAddress("Tonson Apartment");
        doctorDTO2.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctorDTO2.setPhone("097874634");
        doctorDTO2.setGenderType(GenderType.MALE);

        Doctor doctor1 = new Doctor();
        doctor1.setName("Doctor 1");
        doctor1.setAddress("Tonson Apartment");
        doctor1.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor1.setPhone("097874634");
        doctor1.setGenderType(GenderType.MALE);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Doctor 1");
        doctor2.setAddress("Tonson Apartment");
        doctor2.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor2.setPhone("097874634");
        doctor2.setGenderType(GenderType.MALE);

        List<Doctor> doctorList = List.of(doctor1,doctor2);

        Page<Doctor> page = new PageImpl<>(doctorList,pageable,2);

        List<DoctorDTO> doctorDTOList = List.of(doctorDTO1,doctorDTO2);
        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setDoctors(doctorDTOList);
        doctorResponse.setTotalPages(1);
        doctorResponse.setTotalElements(2L);
        doctorResponse.setPageNumber(0);
        doctorResponse.setPageSize(10);
        doctorResponse.setLastPage(true);

        when(doctorRepository.findAll(pageable)).thenReturn(page);

        DoctorResponse result = doctorService.getAllDoctors(0,10,"id","asc");

        Assertions.assertThat(result.getDoctors().size()).isEqualTo(2);

    }

    @Test
    void testDeleteDoctorById_Success() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("Doctor 1");
        doctor1.setAddress("Tonson Apartment");
        doctor1.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor1.setPhone("097874634");
        doctor1.setGenderType(GenderType.MALE);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));
        doctorService.deleteById(1L);

        verify(doctorRepository).delete(doctor1);
    }

}
