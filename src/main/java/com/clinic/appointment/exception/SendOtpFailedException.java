package com.clinic.appointment.exception;

public class SendOtpFailedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public SendOtpFailedException(String message) {
        super(message);
    }
}