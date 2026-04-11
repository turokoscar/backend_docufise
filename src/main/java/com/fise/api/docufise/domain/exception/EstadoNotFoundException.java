package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class EstadoNotFoundException extends DomainException {
    
    public EstadoNotFoundException(Integer id) {
        super("Estado no encontrado con ID: " + id, "ESTADO_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}