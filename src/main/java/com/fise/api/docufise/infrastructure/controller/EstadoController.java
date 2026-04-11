package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.EstadoExpedienteEntity;
import com.fise.api.docufise.domain.repository.IEstadoExpedienteRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estados")
@Tag(name = "Estados", description = "Gestión de estados de expediente")
public class EstadoController {
    
    private final IEstadoExpedienteRepository repository;
    
    public EstadoController(IEstadoExpedienteRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<EstadoExpedienteEntity>>> listar() {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(repository.findAll()));
    }
}