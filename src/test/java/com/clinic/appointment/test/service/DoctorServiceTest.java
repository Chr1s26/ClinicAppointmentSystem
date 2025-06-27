package com.clinic.appointment.test.service;

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
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    public void doctorService_CreateDoctor_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Doctor");
        doctor.setAddress("Tonson Apartment");
        doctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor.setPhone("097874634");
        doctor.setGenderType(GenderType.MALE);

        Model model = mock(Model.class);

//        when(doctorRepository.findDoctorByNameIgnoreCase(doctor.getName())).thenReturn(Optional.empty());
//        when(doctorRepository.findByPhone(doctor.getPhone())).thenReturn(Optional.empty());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorService.createDoctor(doctor,model);

        Assertions.assertThat(savedDoctor).isNotNull();
        Assertions.assertThat(savedDoctor.getName()).isEqualTo("Doctor");
    }

    @Test
    public void doctorService_UpdateDoctor_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Doctor");
        doctor.setAddress("Tonson Apartment");
        doctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        doctor.setPhone("097874634");
        doctor.setGenderType(GenderType.MALE);

        Doctor updateddoctor = new Doctor();
        updateddoctor.setName("Doctor 1");
        updateddoctor.setAddress("Tonson Apartment");
        updateddoctor.setDateOfBirth(LocalDate.of(1990, 01, 01));
        updateddoctor.setPhone("097874634");
        updateddoctor.setGenderType(GenderType.MALE);

        Model model = mock(Model.class);

//        when(doctorRepository.findDoctorByNameIgnoreCase(doctor.getName())).thenReturn(Optional.empty());
//        when(doctorRepository.findByPhone(doctor.getPhone())).thenReturn(Optional.empty());
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenReturn(updateddoctor);

        Doctor doctor1 = doctorService.updateDoctor(1L,doctor);

        assertNotNull(doctor1);
        assertEquals("Doctor 1", doctor1.getName());
    }


}
