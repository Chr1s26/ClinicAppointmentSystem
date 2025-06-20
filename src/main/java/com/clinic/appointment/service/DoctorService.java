package com.clinic.appointment.service;

import com.clinic.appointment.dto.DepartmentDTO;
import com.clinic.appointment.dto.DepartmentResponse;
import com.clinic.appointment.dto.DoctorDTO;
import com.clinic.appointment.dto.DoctorResponse;
import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Doctor createDoctor(Doctor doctor, Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(doctor,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("doctor", doctor);
            throw new CommonException(errorMessages,"doctors/create",model);
        }

        doctor =this.doctorRepository.save(doctor);
        return doctor;
    }

    public Doctor updateDoctor(Long id,Doctor doctor,Model model) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(doctor,errorMessages);

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
            updatedDoctor.setGenderType(doctor.getGenderType());
            doctor =this.doctorRepository.save(updatedDoctor);
            return doctor;
        }
        return null;
    }

    public List<Doctor> getDoctors(){
        List<Doctor> doctors =this.doctorRepository.findAll();
        return doctors;
    }

    public DoctorResponse getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        List<Doctor> doctors = doctorPage.getContent();
        List<DoctorDTO> doctorDTOS = doctors.stream().map(doctor -> modelMapper.map(doctor,DoctorDTO.class)).toList();
        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setDoctors(doctorDTOS);
        doctorResponse.setTotalElements(doctorPage.getTotalElements());
        doctorResponse.setTotalPages(doctorPage.getTotalPages());
        doctorResponse.setPageNumber(doctorPage.getNumber());
        doctorResponse.setPageSize(doctorPage.getSize());
        doctorResponse.setLastPage(doctorPage.isLast());
        return doctorResponse;
    }

    public Doctor getDoctorById(Long id){
        return this.doctorRepository.findById(id).orElseThrow();
    }

    public List<Doctor> findAll() {
        return this.doctorRepository.findAll();
    }

    public void deleteById(Long id) {
        Optional<Doctor> doctorOp = this.doctorRepository.findById(id);
        doctorOp.ifPresent(this.doctorRepository::delete);
    }



    //validation
    //ValidationMethod
    private void validate(Doctor doctor,List<ErrorMessage> errorMessages){
        validateField(doctor.getName(),"nameError","Doctor Name can't be empty",errorMessages);
        validateField(doctor.getPhone(),"phoneError","Doctor phone can't be empty",errorMessages);
        validateField(doctor.getAddress(),"addressError","Doctor address can't be empty",errorMessages);
        checkDuplicateName(doctor, "nameError", "Doctor name already exists", errorMessages);
        checkDuplicatePhone(doctor, "phoneError", "Phone number already exists", errorMessages);
    }

    //Not Null Method
    private void validateField(String value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(StringUtil.isEmpty(value)) {
            addError(fieldName,message,errorMessages);
        }
    }

    //Duplicate Method
    private void checkDuplicateName(Doctor doctor, String fieldName, String message, List<ErrorMessage> errorMessages) {
        Optional<Doctor> existingDoctor;

        if (doctor.getId() == null) {
            existingDoctor = doctorRepository.findDoctorByNameIgnoreCase(doctor.getName());
        } else {
            existingDoctor = doctorRepository.findDoctorByName(doctor.getId(), doctor.getName());
        }

        existingDoctor.ifPresent(d -> addError(fieldName, message, errorMessages));
    }

    private void checkDuplicatePhone(Doctor doctor, String fieldName, String message, List<ErrorMessage> errorMessages) {
        Optional<Doctor> existingDoctor;

        if (doctor.getId() == null) {
            existingDoctor = doctorRepository.findByPhone(doctor.getPhone());
        } else {
            existingDoctor = doctorRepository.findDoctorByPhone(doctor.getId(), doctor.getPhone());
        }

        existingDoctor.ifPresent(d -> addError(fieldName, message, errorMessages));
    }

    //adding Error
    private void addError(String field, String msg, List<ErrorMessage> list) {
        list.add(new ErrorMessage(field, msg));
    }
}
