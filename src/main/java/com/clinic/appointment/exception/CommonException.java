package com.clinic.appointment.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Data
@AllArgsConstructor
public class CommonException extends RuntimeException {
    private List<ErrorMessage> errorMessageList;
    private String returnRoute;
    private Model model;
}
