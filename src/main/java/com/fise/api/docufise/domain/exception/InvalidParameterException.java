package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends DomainException {
    
    public InvalidParameterException(String message) {
        super(message, "INVALID_PARAMETER", HttpStatus.BAD_REQUEST);
    }
}