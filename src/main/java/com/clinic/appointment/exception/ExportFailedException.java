package com.clinic.appointment.exception;

import lombok.Getter;

@Getter
public class ExportFailedException extends RuntimeException {
    private final String view;
    private final String defaultMessage;

    public ExportFailedException(String view,
                                 String defaultMessage) {
        super(defaultMessage);
        this.view = view;
        this.defaultMessage = defaultMessage;
    }

    public ExportFailedException(String defaultMessage){
        super(defaultMessage);
        this.view = null;
        this.defaultMessage = defaultMessage;
    }
}
