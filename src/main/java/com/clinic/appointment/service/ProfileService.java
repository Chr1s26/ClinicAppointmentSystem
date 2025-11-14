//package com.clinic.appointment.service;
//
//import com.clinic.appointment.dto.ProfileDTO;
//import com.clinic.appointment.model.*;
//import com.clinic.appointment.model.constant.FileType;
//import com.clinic.appointment.repository.AdminRepository;
//import com.clinic.appointment.repository.DoctorRepository;
//import com.clinic.appointment.repository.PatientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class ProfileService {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//    @Autowired
//    private PatientRepository patientRepository;
//    @Autowired
//    private AdminRepository adminRepository;
//    @Autowired
//    private FileService fileService;
//    @Autowired
//    private AuthService authService;
//
//    public ProfileDTO getProfileUrl(String activeRole){
//        AppUser appUser = authService.getCurrentUser();
//        String username = null;
//        String profileUrl = null;
//        String email = appUser.getEmail();
//
//        if("ROLE_DOCTOR".equals(activeRole)){
//            Optional<Doctor> doctorOp = doctorRepository.findDoctorByAppUser(appUser);
//            if(doctorOp.isPresent()){
//                Doctor doctor = doctorOp.get();
//                profileUrl = this.fileService.getFileName(FileType.DOCTOR,doctor.getId());
//            }
//            username = doctorOp.get().getName();
//        }else if("ROLE_PATIENT".equals(activeRole)){
//            Optional<Patient> patientOp = patientRepository.findPatientByAppUser(appUser);
//            if(patientOp.isPresent()){
//                Patient patient = patientOp.get();
//                profileUrl = this.fileService.getFileName(FileType.PATIENT,patient.getId());
//            }
//            username = patientOp.get().getName();
//        }else if("ROLE_ADMIN".equals(activeRole)){
//            Optional<Admin> adminOp = adminRepository.findAdminByAppUser(appUser);
//            if(adminOp.isPresent()){
//                Admin admin = adminOp.get();
//                profileUrl = this.fileService.getFileName(FileType.ADMIN,admin.getId());
//            }
//            username = adminOp.get().getName();
//        }
//
//        ProfileDTO profileDTO = new ProfileDTO(username,profileUrl,email);
//        return profileDTO;
//    }
//    public String extractPrimaryRole(Set<Role> roles) {
//        if (roles == null || roles.isEmpty()) return null;
//
//        for (Role r : roles) {
//            String name = r.getRoleName();
//
//            if (name.equals("ROLE_ADMIN")) return "ROLE_ADMIN";
//            if (name.equals("ROLE_DOCTOR")) return "ROLE_DOCTOR";
//            if (name.equals("ROLE_PATIENT")) return "ROLE_PATIENT";
//        }
//
//        return null; // Unexpected case
//    }
//
//}
