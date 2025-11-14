package com.clinic.appointment.exception;

public class OtpInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public OtpInvalidException(String message) {
        super(message);
    }
}