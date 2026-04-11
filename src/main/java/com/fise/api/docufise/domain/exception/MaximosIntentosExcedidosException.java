package com.fise.api.docufise.domain.exception;

import org.springframework.http.HttpStatus;

public class MaximosIntentosExcedidosException extends DomainException {
    
    public MaximosIntentosExcedidosException(Integer intentosMaximos) {
        super("Excedidos " + intentosMaximos + " intentos fallidos. Usuario bloqueado por 15 minutos", "MAX_INTENTOS_EXCEDIDOS", HttpStatus.LOCKED);
    }
}