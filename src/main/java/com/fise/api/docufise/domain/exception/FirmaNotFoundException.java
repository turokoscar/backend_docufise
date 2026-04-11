package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class FirmaNotFoundException extends DomainException {
    
    public FirmaNotFoundException(Integer id) {
        super("Firma no encontrada con ID: " + id, "FIRMA_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}