package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class UsuarioInactivoException extends DomainException {
    
    public UsuarioInactivoException() {
        super("Usuario inactivo", "USUARIO_INACTIVE", HttpStatus.FORBIDDEN);
    }
}