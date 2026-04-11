package com.fise.api.docufise.infrastructure.config;

import com.fise.api.docufise.shared.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Manejador global de excepciones para centralizar las respuestas de error.
 * Adaptado de la lógica de ExceptionMiddleware de .NET.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        log.error("Excepción no manejada: ", ex);
        
        HttpStatus status = getHttpStatus(ex);
        String message = getUserFriendlyMessage(ex);
        
        return new ResponseEntity<>(
                ApiResponse.error(message, status.value()),
                status
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoSuchElementException ex) {
        return new ResponseEntity<>(
                ApiResponse.notFound(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), 400),
                HttpStatus.BAD_REQUEST
        );
    }

    private String getUserFriendlyMessage(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        
        if (message.contains("no devuelve") || message.contains("not found")) {
            return "No se encontraron registros";
        }
        if (message.contains("sesión") || message.contains("expired") || message.contains("unauthorized")) {
            return "Sesión expirada o no autorizada. Vuelva a iniciar";
        }
        if (message.contains("valid") || message.contains("formato") || message.contains("incorrect")) {
            return "Datos inválidos o formato incorrecto";
        }
        
        return "Error interno del servidor. Contacte al administrador.";
    }

    private HttpStatus getHttpStatus(Exception ex) {
        if (ex instanceof NoSuchElementException) return HttpStatus.NOT_FOUND;
        if (ex instanceof IllegalArgumentException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof SecurityException) return HttpStatus.UNAUTHORIZED;
        
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
