package com.clinic.appointment.exception;

import lombok.Data;

@Data
public class ErrorMessage {
    private String fieldName;
    private String message;

    public ErrorMessage(String fieldName,String message){
        this.fieldName = fieldName;
        this.message = message;
    }
}
