package com.clinic.appointment.exception;

import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ModelAttribute("requestURI")
//    public String requestURL(HttpServletRequest request) {
//        return request.getRequestURI();
//    }
    @Autowired
    private DepartmentService departmentService;

    @ExceptionHandler(CommonException.class)
    public ModelAndView handleErrorMessage(CommonException e, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(e.getReturnRoute());
        Model model = e.getModel();

        if(model != null) {
            Map<String, Object> attrs = e.getModel().asMap();
            for(Map.Entry<String, Object> entry : attrs.entrySet()) {
                modelAndView.addObject(entry.getKey(), entry.getValue());
            }
        }

        modelAndView.addObject("genderType", GenderType.values());
        modelAndView.addObject("patientType", PatientType.values());

        for(ErrorMessage errorMessage : e.getErrorMessageList()){
            modelAndView.addObject(errorMessage.getFieldName(), errorMessage.getMessage());
        }

        modelAndView.addObject("requestURI", request.getRequestURI());

        return modelAndView;
    }

}
