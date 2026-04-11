package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class TipoDocumentoNotFoundException extends DomainException {
    
    public TipoDocumentoNotFoundException(Integer id) {
        super("Tipo de documento no encontrado con ID: " + id, "TIPO_DOCUMENTO_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}