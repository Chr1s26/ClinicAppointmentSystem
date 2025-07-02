package com.clinic.appointment.test.repository;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.GenderType;
import com.clinic.appointment.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    private Doctor doctor1;
    private Doctor doctor2;

    @BeforeEach
    void setup() {
        doctor1 = new Doctor();
        doctor1.setName("Alice");
        doctor1.setPhone("123456");
        doctor1.setAddress("Bangkok");
        doctor1.setGenderType(GenderType.FEMALE);
        doctor1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctorRepository.save(doctor1);

        doctor2 = new Doctor();
        doctor2.setName("Bob");
        doctor2.setPhone("654321");
        doctor2.setAddress("Chiang Mai");
        doctor2.setGenderType(GenderType.MALE);
        doctor2.setDateOfBirth(LocalDate.of(1985, 5, 15));
        doctorRepository.save(doctor2);
    }

    @Test
    void testFindDoctorByNameIgnoreCase_found() {
        Optional<Doctor> result = doctorRepository.findDoctorByNameIgnoreCase("alice");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void testFindDoctorByNameIgnoreCase_notFound() {
        Optional<Doctor> result = doctorRepository.findDoctorByNameIgnoreCase("noname");
        assertThat(result).isNotPresent();
    }

    @Test
    void testFindDoctorByName_excludingId_found() {
        Optional<Doctor> result = doctorRepository.findDoctorByName(doctor2.getId(), "Alice");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void testFindDoctorByName_excludingId_notFound() {
        Optional<Doctor> result = doctorRepository.findDoctorByName(doctor1.getId(), "Alice");
        assertThat(result).isNotPresent(); // excludes self
    }

    @Test
    void testFindByPhone_found() {
        Optional<Doctor> result = doctorRepository.findByPhone("123456");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void testFindByPhone_notFound() {
        Optional<Doctor> result = doctorRepository.findByPhone("999999");
        assertThat(result).isNotPresent();
    }

    @Test
    void testFindDoctorByPhone_excludingId_found() {
        Optional<Doctor> result = doctorRepository.findDoctorByPhone(doctor2.getId(), "123456");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void testFindDoctorByPhone_excludingId_notFound() {
        Optional<Doctor> result = doctorRepository.findDoctorByPhone(doctor1.getId(), "123456");
        assertThat(result).isNotPresent(); // excludes self
    }
}
