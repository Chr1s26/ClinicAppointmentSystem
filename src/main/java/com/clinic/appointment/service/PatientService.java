package com.clinic.appointment.service;

import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient create(Patient patient, Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validateField(patient.getName(),"nameError","Patient Name can't be empty",errorMessages);
        validateField(patient.getAddress(),"addressError","Patient address can't be empty",errorMessages);
        validateField(patient.getEmail(),"emailError","Patient email can't be empty",errorMessages);
        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient", patient);
            throw new CommonException(errorMessages,"patients/create",model);
        }
        return patientRepository.save(patient);
    }

    private void validateField(String value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(StringUtil.isEmpty(value)) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setFieldName(fieldName);
            errorMessage.setMessage(message);
            errorMessages.add(errorMessage);
        }
    }

    public Patient update(Long id,Patient patient,Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validateField(patient.getName(),"nameError","Patient Name can't be empty",errorMessages);
        validateField(patient.getAddress(),"addressError","Patient address can't be empty",errorMessages);
        validateField(patient.getEmail(),"emailError","Patient email can't be empty",errorMessages);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient",patient);
            throw new CommonException(errorMessages,"patients/edit",model);
        }
        Patient updatePatient = patientRepository.findById(id).orElse(null);
        updatePatient.setName(patient.getName());
        updatePatient.setEmail(patient.getEmail());
        updatePatient.setAddress(patient.getAddress());
        updatePatient.setDateOfBirth(patient.getDateOfBirth());
        return patientRepository.save(updatePatient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public void deleteById(Long id) {
        Patient patient = this.patientRepository.findById(id).orElseThrow();
        patientRepository.delete(patient);
    }
}
