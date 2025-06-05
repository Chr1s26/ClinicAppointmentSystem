package com.clinic.appointment.service;

import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient create(Patient patient, Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(patient,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient", patient);
            throw new CommonException(errorMessages,"patients/create",model);
        }

        return patientRepository.save(patient);
    }

    public Patient update(Long id,Patient patient,Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(patient,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient",patient);
            throw new CommonException(errorMessages,"patients/edit",model);
        }

        Patient updatePatient = patientRepository.findById(id).orElse(null);
        updatePatient.setName(patient.getName());
        updatePatient.setEmail(patient.getEmail());
        updatePatient.setAddress(patient.getAddress());
        updatePatient.setDateOfBirth(patient.getDateOfBirth());
        updatePatient.setPatientType(patient.getPatientType());
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



    //validation
    //validateMethod
    private void validate(Patient patient,List<ErrorMessage> errorMessages){
        checkDuplicateName(patient, "nameError", "Patient name already exists", errorMessages);
        checkDuplicateEmail(patient, "emailError", "Patient email already exists", errorMessages);
        validateField(patient.getName(),"nameError","Patient Name can't be empty",errorMessages);
        validateField(patient.getAddress(),"addressError","Patient address can't be empty",errorMessages);
        validateField(patient.getEmail(),"emailError","Patient email can't be empty",errorMessages);

        if (patient.getDateOfBirth() == null) {
            addError("dateOfBirthError", "Patient Date of Birth can't be empty", errorMessages);
        } else {
            validateDateOfBirth(patient.getDateOfBirth(), "dateOfBirthError", "Date of birth is not valid", errorMessages);
        }
    }

    //Not Null Method
    private void validateField(String value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(StringUtil.isEmpty(value)) {
            addError(fieldName,message,errorMessages);
        }
    }

    //Check Duplicate Method
    private void checkDuplicateName(Patient patient, String fieldName, String message, List<ErrorMessage> errorMessages) {
        Optional<Patient> existingPatient;

        if (patient.getId() == null) {
            existingPatient = patientRepository.findPatientByNameIgnoreCase(patient.getName());
        } else {
            existingPatient = patientRepository.findPatientByName(patient.getId(), patient.getName());
        }

        existingPatient.ifPresent(p -> addError(fieldName, message, errorMessages));
    }

    private void checkDuplicateEmail(Patient patient, String fieldName, String message, List<ErrorMessage> errorMessages) {
        Optional<Patient> existingPatient;

        if (patient.getId() == null) {
            existingPatient = patientRepository.findByEmail(patient.getEmail());
        } else {
            existingPatient = patientRepository.findPatientByEmail(patient.getId(), patient.getEmail());
        }

        existingPatient.ifPresent(p -> addError(fieldName, message, errorMessages));
    }

    //Check Date of Birth Logical Error
    private void validateDateOfBirth(LocalDate dateOfBirth, String fieldName, String message, List<ErrorMessage> errorMessages) {
        LocalDate today = LocalDate.now();
        if (dateOfBirth.isAfter(today)) {
            addError(fieldName,message,errorMessages);
        }
    }

    //adding Error
    private void addError(String field, String msg, List<ErrorMessage> list) {
        list.add(new ErrorMessage(field, msg));
    }
}
