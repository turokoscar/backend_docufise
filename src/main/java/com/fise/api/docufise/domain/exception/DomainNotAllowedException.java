package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class DomainNotAllowedException extends DomainException {
    
    public DomainNotAllowedException(String message) {
        super(message, "DOMAIN_NOT_ALLOWED", HttpStatus.BAD_REQUEST);
    }
}