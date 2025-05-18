package com.clinic.appointment.service;

import com.clinic.appointment.model.Patient;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient update(Long id,Patient patient) {
        Patient updatePatient = patientRepository.findById(id).orElse(null);
        updatePatient.setName(patient.getName());
        updatePatient.setEmail(patient.getEmail());
        updatePatient.setAddress(patient.getAddress());
        updatePatient.setDateOfBirth(patient.getDateOfBirth());
        return patientRepository.save(patient);
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
