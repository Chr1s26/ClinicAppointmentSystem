package com.clinic.appointment.service;
import com.clinic.appointment.dto.doctor.DoctorCreateDto;
import com.clinic.appointment.dto.doctor.DoctorDTO;
import com.clinic.appointment.dto.doctor.DoctorResponse;
import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.Doctor;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.Status;
import com.clinic.appointment.repository.AppUserRepository;
import com.clinic.appointment.repository.DoctorRepository;
import com.clinic.appointment.util.AgeCalculator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final ModelMapper modelMapper;

    private final MasterDataPolicy masterDataPolicy;

    @Autowired
    private  FileService fileService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AppUserRepository appUserRepository;

    public Doctor createDoctor(DoctorCreateDto createDoctor, Model model) {
        Doctor doctor = new Doctor();
        doctor.setName(createDoctor.getName());
        doctor.setPhone(createDoctor.getPhone());
        doctor.setGenderType(createDoctor.getGenderType());
        doctor.setAddress(createDoctor.getAddress());
        doctor.setDateOfBirth(createDoctor.getDateOfBirth());
        doctor.setCreatedAt(LocalDate.now());
        doctor.setCreatedBy(authService.getCurrentUser());
        doctor.setStatus(Status.ACTIVE.name());
        AppUser appUser = appUserRepository.findById(createDoctor.getAppUserId())
                .orElseThrow(() -> new RuntimeException("AppUser not found"));
        doctor.setAppUser(appUser);
        appUser.setDoctor(doctor);

        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(doctor,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("doctor", doctor);
            throw new CommonException(errorMessages,"doctors/create",model);
        }
        if(masterDataPolicy.canCreate(doctor)) {
            doctor =this.doctorRepository.save(doctor);
            if(createDoctor.getFile() != null && !createDoctor.getFile().isEmpty()){
                fileService.handleFileUpload(createDoctor.getFile(), FileType.DOCTOR, doctor.getId(), "s3");
                return doctor;
            }
        }else{
            throw new AccessDeniedException("Access denied");
        }
        return null;
    }


    public Doctor updateDoctor(Long id,DoctorDTO doctorDTO, Model model) {

        Doctor doctor = this.convertToEntity(doctorDTO);
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(doctor,errorMessages);

        if(!errorMessages.isEmpty()) {
            DoctorDTO doctorDTO1 = convertToDTO(doctor);
            model.addAttribute("doctor", doctorDTO1);
            throw new CommonException(errorMessages,"doctors/edit",model);
        }

        Optional<Doctor> updatedDoctorOp = this.doctorRepository.findById(id);
        if(updatedDoctorOp.isPresent()){
            Doctor updatedDoctor = updatedDoctorOp.get();
            updatedDoctor.setName(doctor.getName());
            updatedDoctor.setAddress(doctor.getAddress());
            updatedDoctor.setPhone(doctor.getPhone());
            updatedDoctor.setGenderType(doctor.getGenderType());
            updatedDoctor.setDateOfBirth(doctor.getDateOfBirth());
            updatedDoctor.setStatus(Status.ACTIVE.name());
            updatedDoctor.setUpdatedBy(authService.getCurrentUser());
            updatedDoctor.setUpdatedAt(LocalDate.now());
            Long id1 = doctorDTO.getAppUserId();
            System.out.println(appUserRepository.findById(doctorDTO.getAppUserId()));
            AppUser appUser = appUserRepository.findById(doctorDTO.getAppUserId())
                    .orElseThrow(() -> new RuntimeException("AppUser not found"));
            updatedDoctor.setAppUser(appUser);
            appUser.setDoctor(updatedDoctor);

            if(masterDataPolicy.canUpdate(updatedDoctor)){
                if(doctorDTO.getFile() != null && !doctorDTO.getFile().isEmpty()){
                    fileService.handleFileUpload(doctorDTO.getFile(), FileType.DOCTOR, doctor.getId(), "s3");
                }
                doctor =this.doctorRepository.save(updatedDoctor);

                return doctor;
            }else{
                throw new AccessDeniedException("Access denied");
            }
        }
        return null;
    }

    public Doctor updateDoctorDepartment(Long id,Doctor doctor) {
        Optional<Doctor> updatedDoctorOp = this.doctorRepository.findById(id);
        if(updatedDoctorOp.isPresent()){
            Doctor updatedDoctor = updatedDoctorOp.get();
            updatedDoctor.setName(doctor.getName());
            updatedDoctor.setAddress(doctor.getAddress());
            updatedDoctor.setPhone(doctor.getPhone());
            updatedDoctor.setGenderType(doctor.getGenderType());
            updatedDoctor.setDateOfBirth(doctor.getDateOfBirth());
            doctor =this.doctorRepository.save(updatedDoctor);
            return doctor;
        }
        return null;
    }


    public List<Doctor> getDoctors(){
        List<Doctor> doctors =this.doctorRepository.findAll();
        return doctors;
    }

    @PreAuthorize("@masterDataPolicy.canView(filterObject)")
    public DoctorResponse getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        if(masterDataPolicy.isAdmin()){
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
            Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
            List<Doctor> doctors = doctorPage.getContent();
            List<DoctorDTO> doctorDTOS = new ArrayList<>();
            for(Doctor doctor : doctors){
                doctorDTOS.add(convertToDTO(doctor));
            }
            DoctorResponse doctorResponse = new DoctorResponse();
            doctorResponse.setDoctors(doctorDTOS);
            doctorResponse.setTotalElements(doctorPage.getTotalElements());
            doctorResponse.setTotalPages(doctorPage.getTotalPages());
            doctorResponse.setPageNumber(doctorPage.getNumber());
            doctorResponse.setPageSize(doctorPage.getSize());
            doctorResponse.setLastPage(doctorPage.isLast());
            return doctorResponse;
        }else{
            throw new AccessDeniedException("Access denied");
        }
    }

    public DoctorDTO getDoctorById(Long id){
         Doctor doctor = this.doctorRepository.findById(id).orElseThrow();
         DoctorDTO doctorDTO = this.convertToDTO(doctor);
         return doctorDTO;
    }

    public Doctor findDoctorById(Long id){
        Doctor doctor = this.doctorRepository.findById(id).orElseThrow();
        return doctor;
    }

    public List<Doctor> findAll() {
        return this.doctorRepository.findAll();
    }


    public void deleteById(Long id) {
        Optional<Doctor> doctorOp = doctorRepository.findById(id);

        if (doctorOp.isPresent()) {
            if(masterDataPolicy.canDestroy(doctorOp.get())){
                Doctor doctor = doctorOp.get();

                AppUser appUser = doctor.getAppUser();
                if (appUser != null) {
                    appUser.setDoctor(null);
                }

                doctor.setAppUser(null);
                doctorRepository.delete(doctor);
            }else{
                throw new AccessDeniedException("Access denied");
            }
        }
    }

    //Converter
    public DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setAddress(doctor.getAddress());
        doctorDTO.setPhone(doctor.getPhone());
        doctorDTO.setGenderType(doctor.getGenderType());
        doctorDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorDTO.setAge(AgeCalculator.calculateAge(doctor.getDateOfBirth()));
        doctorDTO.setProfileUrl(this.fileService.getFileName(FileType.DOCTOR, doctor.getId()));
        return doctorDTO;
    }

    public Doctor convertToEntity(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setGenderType(doctorDTO.getGenderType());
        doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        doctor.setId(doctorDTO.getId());
        return doctor;
    }


    //validation
    //ValidationMethod
    private void validate(Doctor doctor,List<ErrorMessage> errorMessages){
        validateField(doctor.getName(),"nameError","Doctor Name can't be empty",errorMessages);
        validateField(doctor.getPhone(),"phoneError","Doctor phone can't be empty",errorMessages);
        validateField(doctor.getAddress(),"addressError","Doctor address can't be empty",errorMessages);
        validateField(doctor.getDateOfBirth(),"dateOfBirthError","Doctor date of birth can't be empty",errorMessages);
        validateField(doctor.getGenderType(),"genderTypeError","Doctor gender type can't be empty",errorMessages);
        checkDuplicateName(doctor, "nameError", "Doctor name already exists", errorMessages);
        checkDuplicatePhone(doctor, "phoneError", "Phone number already exists", errorMessages);
        validateDateOfBirth(doctor.getDateOfBirth(),"dateOfBirthError","Invalid Date of birth",errorMessages);
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

    private void validateField(GenderType value, String fieldName, String message, List<ErrorMessage> errorMessages) {
        if(value == null) {
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

    private void validateDateOfBirth(LocalDate dateOfBirth, String fieldName, String message, List<ErrorMessage> errorMessages) {
        LocalDate today = LocalDate.now();

        if (dateOfBirth == null) {
            return;
        }

        if (dateOfBirth.isAfter(today)) {
            addError(fieldName,message,errorMessages);
        }
    }

    //adding Error
    private void addError(String field, String msg, List<ErrorMessage> list) {
        list.add(new ErrorMessage(field, msg));
    }
}
