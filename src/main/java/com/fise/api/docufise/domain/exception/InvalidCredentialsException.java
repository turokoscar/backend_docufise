package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends DomainException {
    
    public InvalidCredentialsException() {
        super("Credenciales inválidas", "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
    }
}