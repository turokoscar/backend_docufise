package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class DocumentoNotFoundException extends DomainException {
    
    public DocumentoNotFoundException(Integer id) {
        super("Documento no encontrado con ID: " + id, "DOCUMENTO_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}