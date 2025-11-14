package com.clinic.appointment.exception;

import lombok.Data;

@Data
public class DuplicateException extends RuntimeException {
    private final String objectName;
    private final Object ObjectValue;
    private final String field;
    private final String view;
    private final String messageKey;
    private final String defaultMessage;

    public DuplicateException(String objectName, Object objectValue, String field, String view ,String defaultMessage) {
        super(defaultMessage);
        this.objectName = objectName;
        this.ObjectValue = objectValue;
        this.field = field;
        this.view = view;
        this.messageKey = "duplicate";
        this.defaultMessage = defaultMessage;
    }
}
