package com.clinic.appointment.service;

import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor createDoctor(Doctor doctor, Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validateField(doctor.getName(),"nameError","Doctor Name can't be empty",errorMessages);
        validateField(doctor.getPhone(),"phoneError","Doctor phone can't be empty",errorMessages);
        validateField(doctor.getAddress(),"addressError","Doctor address can't be empty",errorMessages);
        if(!errorMessages.isEmpty()) {
            model.addAttribute("doctor", doctor);
            throw new CommonException(errorMessages,"doctors/create",model);
        }
        doctor =this.doctorRepository.save(doctor);
        return doctor;
    }

    public Doctor updateDoctor(Long id,Doctor doctor,Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validateField(doctor.getName(),"nameError","Doctor Name can't be empty",errorMessages);
        validateField(doctor.getPhone(),"phoneError","Doctor phone can't be empty",errorMessages);
        validateField(doctor.getAddress(),"addressError","Doctor address can't be empty",errorMessages);
        if(!errorMessages.isEmpty()) {
            model.addAttribute("doctor", doctor);
            throw new CommonException(errorMessages,"doctors/edit",model);
        }
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

    private void validateField(String value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(StringUtil.isEmpty(value)) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setFieldName(fieldName);
            errorMessage.setMessage(message);
            errorMessages.add(errorMessage);
        }
    }
}
