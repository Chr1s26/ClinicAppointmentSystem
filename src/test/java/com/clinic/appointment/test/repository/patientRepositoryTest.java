//package com.clinic.appointment.test.repository;
//
//import com.clinic.appointment.model.Patient;
//import com.clinic.appointment.model.constant.PatientType;
//import com.clinic.appointment.repository.PatientRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDate;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//public class patientRepositoryTest {
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @Test
//    public void patientRepository_SaveAll_ReturnsSavedPatient(){
//        Patient patient = new Patient();
//        patient.setName("Dr. Smith");
//        patient.setAddress("Hello Apartment");
//        patient.setEmail("hello44@gmail.com");
//        patient.setPatientType(PatientType.IN_PATIENT);
//        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
//
//        Patient result = patientRepository.save(patient);
//
//        Assertions.assertThat(result).isNotNull();
//        Assertions.assertThat(result.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void patientRepository_UpdatePatient_ReturnPatientNotNull(){
//        Patient patient = new Patient();
//        patient.setName("Dr. Smith");
//        patient.setAddress("Hello Apartment");
//        patient.setEmail("hello44@gmail.com");
//        patient.setPatientType(PatientType.IN_PATIENT);
//        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
//
//        patientRepository.save(patient);
//
//        Patient savedPatient = patientRepository.findById(patient.getId()).get();
//        savedPatient.setName("Dr. John");
//        savedPatient.setAddress("Hello Apartment");
//        savedPatient.setEmail("hello44@gmail.com");
//        savedPatient.setPatientType(PatientType.IN_PATIENT);
//        savedPatient.setDateOfBirth(LocalDate.of(1980, 1, 1));
//
//        Patient result = patientRepository.save(savedPatient);
//
//        Assertions.assertThat(result).isNotNull();
//        Assertions.assertThat(result.getId()).isGreaterThan(0);
//        Assertions.assertThat(result.getName()).isEqualTo("Dr. John");
//    }
//
//}
