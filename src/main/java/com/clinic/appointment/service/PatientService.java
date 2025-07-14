package com.clinic.appointment.service;

import com.clinic.appointment.dto.patient.PatientCreateDto;
import com.clinic.appointment.dto.patient.PatientDTO;
import com.clinic.appointment.dto.patient.PatientResponse;
import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.model.constant.Status;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private FileService fileService;
    @Autowired
    private AuthService authService;

    public Patient create(PatientCreateDto patientCreateDto, Model model) {
        Patient patient = convertToEntity(patientCreateDto);
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(patient,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient", patient);
            throw new CommonException(errorMessages,"patients/create",model);
        }
        Patient savedPatient = patientRepository.save(patient);

        if(patientCreateDto.getFile() != null && !patientCreateDto.getFile().isEmpty()){
            fileService.handleFileUpload(patientCreateDto.getFile(), FileType.PATIENT, savedPatient.getId(),"s3");
        }

        return savedPatient;
    }

    public Patient update(Long id,PatientDTO patientDTO,Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        Patient checkPatient = convertToEntity(patientDTO);
        validate(checkPatient,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("patient",patientDTO);
            throw new CommonException(errorMessages,"patients/edit",model);
        }

        Patient updatePatient = patientRepository.findById(id).orElse(null);
        updatePatient.setName(patientDTO.getName());
        updatePatient.setAddress(patientDTO.getAddress());
        updatePatient.setDateOfBirth(patientDTO.getDateOfBirth());
        updatePatient.setPatientType(patientDTO.getPatientType());
        updatePatient.setUpdatedAt(LocalDate.now());
        updatePatient.setUpdatedBy(authService.getCurrentUser());
        updatePatient.setStatus(Status.ACTIVE.name());
        updatePatient.setPhone(patientDTO.getPhone());
        if(patientDTO.getAppUserId() != null){
            AppUser appUser = appUserRepository.findById(patientDTO.getAppUserId())
                    .orElseThrow(() -> new RuntimeException("AppUser not found"));
            updatePatient.setAppUser(appUser);
            appUser.setPatient(updatePatient);
        }
        if(patientDTO.getFile() != null && !patientDTO.getFile().isEmpty()){
            fileService.handleFileUpload(patientDTO.getFile(), FileType.PATIENT, updatePatient.getId(),"s3");
        }
        return patientRepository.save(updatePatient);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public PatientDTO getPatientById(Long id) {
        Patient patient =findById(id);
        PatientDTO patientDTO = convertToDTO(patient);
        return patientDTO;
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public PatientResponse getAllPatients(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Patient> page = patientRepository.findAll(pageable);
        List<Patient> patients = page.getContent();
        List<PatientDTO> patientDTOs = new ArrayList<>();
        for(Patient patient : patients){
            patientDTOs.add(convertToDTO(patient));
        }
        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setPatients(patientDTOs);
        patientResponse.setPageNumber(page.getNumber());
        patientResponse.setPageSize(page.getSize());
        patientResponse.setTotalPages(page.getTotalPages());
        patientResponse.setTotalElements(page.getTotalElements());
        patientResponse.setLastPage(page.isLast());
        return patientResponse;
    }

    public void deleteById(Long id) {
        Patient patient = this.patientRepository.findById(id).orElseThrow();
        patientRepository.delete(patient);
    }

    //Converter
    public PatientDTO convertToDTO(Patient patient){
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setPhone(patient.getPhone());
        patientDTO.setGenderType(patient.getGenderType());
        patientDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDTO.setPatientType(patient.getPatientType());
        patientDTO.setProfileUrl(this.fileService.getFileName(FileType.PATIENT, patient.getId()));
        return patientDTO;
    }

    public Patient convertToEntity(PatientCreateDto patientCreateDto) {
        Patient patient = new Patient();
        patient.setName(patientCreateDto.getName());
        patient.setPhone(patientCreateDto.getPhone());
        patient.setAddress(patientCreateDto.getAddress());
        patient.setDateOfBirth(patientCreateDto.getDateOfBirth());
        patient.setPatientType(patientCreateDto.getPatientType());
        patient.setStatus(Status.ACTIVE.name());
        patient.setCreatedAt(LocalDate.now());
        patient.setCreatedBy(authService.getCurrentUser());
        if(patientCreateDto.getAppUserId() != null){
            AppUser appUser = appUserRepository.findById(patientCreateDto.getAppUserId())
                    .orElseThrow(() -> new RuntimeException("AppUser not found"));
            patient.setAppUser(appUser);
            appUser.setPatient(patient);
        }
        return patient;
    }

    public Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setPhone(patientDTO.getPhone());
        patient.setAddress(patientDTO.getAddress());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setPatientType(patientDTO.getPatientType());
        patient.setGenderType(patientDTO.getGenderType());
        patient.setStatus(Status.ACTIVE.name());
        patient.setCreatedAt(LocalDate.now());
        patient.setCreatedBy(authService.getCurrentUser());
        AppUser appUser = appUserRepository.findById(patientDTO.getAppUserId())
                .orElseThrow(() -> new RuntimeException("AppUser not found"));
        patient.setAppUser(appUser);
        appUser.setPatient(patient);
        return patient;
    }

    //validation
    //validateMethod
    private void validate(Patient patient,List<ErrorMessage> errorMessages){
        checkDuplicateName(patient, "nameError", "Patient name already exists", errorMessages);
        validateField(patient.getName(),"nameError","Patient Name can't be empty",errorMessages);
        validateField(patient.getAddress(),"addressError","Patient address can't be empty",errorMessages);
        validateField(patient.getDateOfBirth(),"dateOfBirthError","Patient date of birth can't be empty",errorMessages);
        validateField(patient.getPatientType(),"patientTypeError","Patient type can't be empty",errorMessages);

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

    private void validateField(LocalDate value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(value == null) {
            addError(fieldName,message,errorMessages);
        }
    }

    private void validateField(PatientType value, String fieldName, String message, List<ErrorMessage> errorMessages) {
        if(value == null) {
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

//    private void checkDuplicateEmail(Patient patient, String fieldName, String message, List<ErrorMessage> errorMessages) {
//        Optional<Patient> existingPatient;
//
//        if (patient.getId() == null) {
//            existingPatient = patientRepository.findByEmail(patient.getEmail());
//        } else {
//            existingPatient = patientRepository.findPatientByEmail(patient.getId(), patient.getEmail());
//        }
//
//        existingPatient.ifPresent(p -> addError(fieldName, message, errorMessages));
//    }

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
