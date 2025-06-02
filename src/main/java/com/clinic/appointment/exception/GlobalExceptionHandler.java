package com.clinic.appointment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ModelAttribute("requestURI")
//    public String requestURL(HttpServletRequest request) {
//        return request.getRequestURI();
//    }

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
        for(ErrorMessage errorMessage : e.getErrorMessageList()){
            modelAndView.addObject(errorMessage.getFieldName(), errorMessage.getMessage());
        }
        modelAndView.addObject("requestURI", request.getRequestURI());
//        model.addAttribute("requestURI",request.getRequestURI());
//        globalModel.addAttribute("requestURI",request.getRequestURI());
        return modelAndView;
    }

}
