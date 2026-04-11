package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class UsuarioNotFoundException extends DomainException {
    
    public UsuarioNotFoundException(String identifier) {
        super("Usuario no encontrado: " + identifier, "USUARIO_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}