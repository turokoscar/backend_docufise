package com.fise.api.docufise.infrastructure.config;

import com.fise.api.docufise.domain.exception.DomainException;
import com.fise.api.docufise.shared.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleDomainException(DomainException ex) {
        log.warn("Excepción de dominio: [{}] {}", ex.getCode(), ex.getMessage());
        
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), ex.getHttpStatus().value()),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        log.error("Excepción inesperada: ", ex);
        
        return new ResponseEntity<>(
                ApiResponse.error("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}