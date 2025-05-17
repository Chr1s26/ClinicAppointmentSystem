package com.clinic.appointment.service;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor createDoctor(Doctor doctor){
        doctor =this.doctorRepository.save(doctor);
        return doctor;
    }

    public Doctor updateDoctor(Doctor doctor){
        doctor =this.doctorRepository.save(doctor);
        return doctor;
    }

    public List<Doctor> getDoctors(){
        List<Doctor> doctors =this.doctorRepository.findAll();
        return doctors;
    }

    public Doctor getDoctorById(Long id){
        return this.doctorRepository.findById(id).orElseThrow();
    }


    public List<Doctor> findAll() {
        return this.doctorRepository.findAll();
    }
}
