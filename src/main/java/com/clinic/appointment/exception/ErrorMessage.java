package com.clinic.appointment.exception;

import lombok.Data;

@Data
public class ErrorMessage {
    private String fieldName;
    private String message;
}
