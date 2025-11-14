package com.clinic.appointment.controller;

import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ExportFailedException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.exception.UserNotFoundException;
import com.clinic.appointment.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private AppUserService appUserService;
    private DoctorService doctorService;
    private PatientService patientService;
    private DepartmentService departmentService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
        br.addError(new FieldError(
                ex.getObjectName(),
                ex.getField(),
                rejected,
                false,
                new String[]{ex.getMessageKey()},
                null,
                ex.getDefaultMessage()
        ));
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
        model.addAttribute("requestURI", ex.getView());
        return ex.getView();
    }

    @ExceptionHandler(DuplicateException.class)
    public String handleDuplicateField(DuplicateException ex, Model model) {
        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
        br.addError(new FieldError(
                ex.getObjectName(),
                ex.getField(),
                rejected,
                false,
                new String[]{ex.getMessageKey()},
                null,
                ex.getDefaultMessage()
        ));
        addAttributes(ex.getObjectName(),model);
        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
        model.addAttribute("requestURI", ex.getView());
        return ex.getView();
    }

//    @ExceptionHandler(InvalidRoleException.class)
//    public String handleInvalidRoleField(InvalidRoleException ex, Model model) {
//        BindingResult br = new BeanPropertyBindingResult(ex.getObjectValue(), ex.getObjectName());
//        Object rejected = new BeanWrapperImpl(ex.getObjectValue()).getPropertyValue(ex.getField());
//        br.addError(new FieldError(
//                ex.getObjectName(),
//                ex.getField(),
//                rejected,
//                false,
//                new String[]{ex.getMessageKey()},
//                null,
//                ex.getDefaultMessage()
//        ));
//        model.addAttribute(ex.getObjectName(), ex.getObjectValue());
//        model.addAttribute(MODEL_KEY_PREFIX + ex.getObjectName(), br);
//        model.addAttribute("requestURI", ex.getView());
//        return ex.getView();
//    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String handleInternalAuthError(InternalAuthenticationServiceException ex, RedirectAttributes redirectAttributes, Model model) {

        if (ex.getCause() instanceof UserNotFoundException unameEx) {
            model.addAttribute("requestURI", unameEx.getView());
            redirectAttributes.addFlashAttribute("alertMessage", unameEx.getMessage());
            return "redirect:/" + unameEx.getView();
        }
        model.addAttribute("requestURI", "/login");
        redirectAttributes.addFlashAttribute("alertMessage", "Authentication failed");
        return "redirect:/login";
    }

    @ExceptionHandler(ExportFailedException.class)
    public String handleExportFailed(ExportFailedException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("exportError",
                ex.getDefaultMessage() != null ? ex.getDefaultMessage() : "Export failed.");

        return "redirect:" + (ex.getView() != null ? ex.getView() : "");
    }

    public void addAttributes(String object, Model model) {
        if(object.equalsIgnoreCase("admin") || object.equalsIgnoreCase("doctor") || object.equalsIgnoreCase("patient")) {
            model.addAttribute("users", appUserService.findAllUsers());
        }else if(object.equalsIgnoreCase("appointment")) {
            model.addAttribute("doctors", this.doctorService.findAll());
            model.addAttribute("patients", this.patientService.findAllPatients());
            model.addAttribute("departments", this.departmentService.findAllDepartments());
        }
    }

}
