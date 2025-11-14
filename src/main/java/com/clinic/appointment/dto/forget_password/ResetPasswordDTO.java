package com.clinic.appointment.dto.forget_password;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String password;
    private String confirmPassword;
}

