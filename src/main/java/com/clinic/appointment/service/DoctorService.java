package com.clinic.appointment.service;

import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor createDoctor(Doctor doctor){
        doctor =this.doctorRepository.save(doctor);
        return doctor;
    }

    public Doctor updateDoctor(Long id,Doctor doctor){
        Optional<Doctor> updatedDoctorOp = this.doctorRepository.findById(id);
        if(updatedDoctorOp.isPresent()){
            Doctor updatedDoctor = updatedDoctorOp.get();
            updatedDoctor.setName(doctor.getName());
            updatedDoctor.setAddress(doctor.getAddress());
            updatedDoctor.setPhone(doctor.getPhone());
            doctor =this.doctorRepository.save(updatedDoctor);
            return doctor;
        }
        return null;
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

    public void destory(Long id) {
        Optional<Doctor> doctorOp = this.doctorRepository.findById(id);
        doctorOp.ifPresent(this.doctorRepository::delete);
    }
}
