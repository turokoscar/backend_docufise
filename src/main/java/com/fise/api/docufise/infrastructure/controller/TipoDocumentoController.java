package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.TipoDocumento;
import com.fise.api.docufise.domain.repository.ITipoDocumentoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tipos-documento")
@Tag(name = "Tipos de Documento", description = "Gestión de tipos de documento")
public class TipoDocumentoController {
    
    private final ITipoDocumentoRepository repository;
    
    public TipoDocumentoController(ITipoDocumentoRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<TipoDocumento>>> listar() {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(repository.findAll()));
    }
}