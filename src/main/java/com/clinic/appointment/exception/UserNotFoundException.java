package com.clinic.appointment.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException{
    private final String view;

    public UserNotFoundException(String message,String view) {
        super(message);
        this.view = view;
    }
}
