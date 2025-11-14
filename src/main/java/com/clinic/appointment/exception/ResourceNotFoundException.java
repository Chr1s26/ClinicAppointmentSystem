package com.clinic.appointment.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    private final String objectName;
    private final Object ObjectValue;
    private final String field;
    private final String view;
    private final String messageKey;
    private final String defaultMessage;

    public ResourceNotFoundException(String objectName, Object objectValue, String field, String view ,String defaultMessage) {
        super(defaultMessage);
        this.objectName = objectName;
        this.ObjectValue = objectValue;
        this.field = field;
        this.view = view;
        this.messageKey = "resourceNotFound";
        this.defaultMessage = defaultMessage;
    }

}