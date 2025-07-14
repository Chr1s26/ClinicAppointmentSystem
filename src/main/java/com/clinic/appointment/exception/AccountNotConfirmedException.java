package com.clinic.appointment.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountNotConfirmedException extends AuthenticationException {
    public AccountNotConfirmedException(String message){
        super(message);
    }
}
