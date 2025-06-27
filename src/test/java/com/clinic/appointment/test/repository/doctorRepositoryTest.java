package com.clinic.appointment.test.repository;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.GenderType;
import com.clinic.appointment.repository.DoctorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class doctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void doctorRepository_SaveAll_ReturnSavedDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor 1");
        doctor.setAddress("Doctor Address");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));

        Doctor savedDoctor = doctorRepository.save(doctor);

        Assertions.assertThat(savedDoctor).isNotNull();
        Assertions.assertThat(savedDoctor.getId()).isGreaterThan(0);
    }

    @Test
    public void doctorRepository_UpdateDoctor_ReturnDoctorNotNull(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor 1");
        doctor.setAddress("Doctor Address");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));

        doctorRepository.save(doctor);

        Doctor savedDoctor = doctorRepository.findById(doctor.getId()).get();
        savedDoctor.setName("Doctor");
        savedDoctor.setAddress("Doctor Address");
        savedDoctor.setPhone("097834724");
        savedDoctor.setGenderType(GenderType.MALE);
        savedDoctor.setDateOfBirth(LocalDate.of(1990,12,12));

        Doctor updatedDoctor = doctorRepository.save(savedDoctor);

        Assertions.assertThat(updatedDoctor).isNotNull();
        Assertions.assertThat(updatedDoctor.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedDoctor.getName()).isEqualTo(savedDoctor.getName());
    }

    @Test
    public void doctorRepository_DoctorDelete_ReturnDoctorIsEmpty(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor 1");
        doctor.setAddress("Doctor Address");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));

        doctorRepository.save(doctor);

        doctorRepository.deleteById(doctor.getId());
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor.getId());

        Assertions.assertThat(doctorOptional).isEmpty();
    }

    @Test
    public void doctorRepository_GetAll_ReturnMoreThenOneDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor 1");
        doctor.setAddress("Doctor Address");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));
        Doctor doctor1 = new Doctor();
        doctor.setName("Doctor 2");
        doctor.setAddress("Doctor Address 2");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.FEMALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));

        doctorRepository.save(doctor);
        doctorRepository.save(doctor1);

        List<Doctor> doctors = doctorRepository.findAll();

        Assertions.assertThat(doctors).isNotNull();
        Assertions.assertThat(doctors.size()).isEqualTo(2);
    }

    @Test
    public void doctorRepository_FindById_ReturnDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("Doctor 1");
        doctor.setAddress("Doctor Address");
        doctor.setPhone("097834723");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990,12,12));

        doctorRepository.save(doctor);
        Doctor savedDoctor = doctorRepository.findById(doctor.getId()).get();
        Assertions.assertThat(savedDoctor).isNotNull();
    }

    @Test
    public void findDoctorByName_WithDifferentId_ReturnDoctor(){

        Doctor doctor1 = new Doctor();
        doctor1.setName("John Doe");
        doctor1.setAddress("Address 1");
        doctor1.setPhone("0123456789");
        doctor1.setGenderType(GenderType.MALE);
        doctor1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        Doctor savedDoctor1 = doctorRepository.save(doctor1);


        Doctor doctor2 = new Doctor();
        doctor2.setName("john doe");
        doctor2.setAddress("Address 2");
        doctor2.setPhone("0987654321");
        doctor2.setGenderType(GenderType.MALE);
        doctor2.setDateOfBirth(LocalDate.of(1992, 2, 2));
        Doctor savedDoctor2 = doctorRepository.save(doctor2);


        Optional<Doctor> result = doctorRepository.findDoctorByName(savedDoctor1.getId(), "john doe");


        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedDoctor2.getId());
    }

    @Test
    public void findDoctorByNameIgnoreCase_WithMatchingName_ReturnsDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("John Doe");
        doctor.setAddress("Address 1");
        doctor.setPhone("0123456789");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctorRepository.save(doctor);

        Doctor result = doctorRepository.findDoctorByNameIgnoreCase("John Doe").get();

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void findDoctorByPhone_WithDifferentId_ReturnsDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("John Max");
        doctor.setAddress("Address 1");
        doctor.setPhone("0123456789");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctorRepository.save(doctor);

        Doctor doctor1 = new Doctor();
        doctor1.setName("John Doe");
        doctor1.setAddress("Address 1");
        doctor1.setPhone("0123456789");
        doctor1.setGenderType(GenderType.MALE);
        doctor1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctorRepository.save(doctor1);

        Doctor result = doctorRepository.findDoctorByPhone(doctor1.getId(),"0123456789").get();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getPhone()).isEqualTo(doctor1.getPhone());
    }

    @Test
    public void findByPhone_WithExistingPhone_ReturnsDoctor(){
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setPhone("0911222333");
        doctor.setAddress("Clinic Address");
        doctor.setGenderType(GenderType.MALE);
        doctor.setDateOfBirth(LocalDate.of(1980, 1, 1));
        Doctor savedDoctor = doctorRepository.save(doctor);

        Optional<Doctor> result = doctorRepository.findByPhone("0911222333");

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(savedDoctor.getId());
    }
}
