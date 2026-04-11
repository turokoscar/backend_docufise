package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class UsuarioBloqueadoException extends DomainException {
    
    public UsuarioBloqueadoException(Integer minutos) {
        super("Usuario bloqueado temporalmente por " + minutos + " minutos", "USUARIO_BLOQUEADO", HttpStatus.LOCKED);
    }
}