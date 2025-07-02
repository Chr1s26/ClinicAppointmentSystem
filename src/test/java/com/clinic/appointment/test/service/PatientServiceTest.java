package com.clinic.appointment.test.service;

import com.clinic.appointment.dto.PatientResponse;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.repository.PatientRepository;
import com.clinic.appointment.service.PatientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void patientService_CreatePatient_ReturnPatient() {
        Patient patient = new Patient();
        patient.setName("Patient");
        patient.setAddress("Sathorn Road");
        patient.setDateOfBirth(LocalDate.of(1995, 5, 15));
        patient.setEmail("patient@example.com");
        patient.setPatientType(PatientType.OUT_PATIENT);

        when(patientRepository.findPatientByNameIgnoreCase(patient.getName())).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientService.create(patient, null);

        Assertions.assertThat(savedPatient).isNotNull();
        Assertions.assertThat(savedPatient.getName()).isEqualTo("Patient");
    }

    @Test
    public void patientService_UpdatePatient_ReturnPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Updated Patient");
        patient.setAddress("New Address");
        patient.setDateOfBirth(LocalDate.of(1995, 5, 15));
        patient.setEmail("updated@example.com");
        patient.setPatientType(PatientType.OUT_PATIENT);

        Patient existing = new Patient();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setAddress("Old Address");
        existing.setDateOfBirth(LocalDate.of(1995, 5, 15));
        existing.setEmail("old@example.com");
        existing.setPatientType(PatientType.IN_PATIENT);

        when(patientRepository.findPatientByName(patient.getId(), patient.getName())).thenReturn(Optional.empty());
        when(patientRepository.findPatientByEmail(patient.getId(), patient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(existing)).thenReturn(patient);

        Patient updatedPatient = patientService.update(1L, patient, null);

        assertNotNull(updatedPatient);
        assertEquals("Updated Patient", updatedPatient.getName());
    }

    @Test
    public void patientService_FindById_ReturnPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Patient");
        patient.setEmail("patient@example.com");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.findById(1L);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void patientService_GetAllPatientsPagination_ReturnList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Patient patient1 = new Patient();
        patient1.setName("Patient One");
        patient1.setEmail("one@example.com");

        Patient patient2 = new Patient();
        patient2.setName("Patient Two");
        patient2.setEmail("two@example.com");

        List<Patient> patientList = List.of(patient1, patient2);
        Page<Patient> page = new PageImpl<>(patientList, pageable, 2);

        when(patientRepository.findAll(pageable)).thenReturn(page);

        PatientResponse response = patientService.getAllPatients(0, 10, "id", "asc");

        assertEquals(2, response.getPatients().size());
    }

    @Test
    public void patientService_DeleteById_Success() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Patient");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deleteById(1L);

        verify(patientRepository).delete(patient);
    }
}
