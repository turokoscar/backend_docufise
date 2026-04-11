package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Area;
import com.fise.api.docufise.domain.repository.IAreaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@Tag(name = "Áreas", description = "Gestión de áreas organizacionales")
public class AreaController {
    
    private final IAreaRepository iAreaRepository;
    
    public AreaController(IAreaRepository iAreaRepository) {
        this.iAreaRepository = iAreaRepository;
    }
    
    @Operation(summary = "Listar áreas", description = "Retorna todas las áreas del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Area>>> listarTodas() {
        List<Area> lista = iAreaRepository.findAll();
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(lista, "Listado de áreas", lista.size()));
    }
    
    @Operation(summary = "Buscar área", description = "Obtiene un área por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área encontrada"),
            @ApiResponse(responseCode = "404", description = "Área no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Area>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return iAreaRepository.findById(id)
                .map(area -> ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(area)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Área no encontrada")));
    }
    
    @Operation(summary = "Crear área", description = "Registra una nueva área")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Area>> crear(@RequestBody Area area) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(iAreaRepository.save(area)));
    }
    
    @Operation(summary = "Actualizar área", description = "Actualiza un área existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área actualizada"),
            @ApiResponse(responseCode = "404", description = "Área no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Area>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Area area) {
        return iAreaRepository.findById(id)
                .map(existente -> {
                    area.setId(id);
                    return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(iAreaRepository.save(area)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Área no encontrada")));
    }
    
    @Operation(summary = "Eliminar área", description = "Elimina un área por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área eliminada"),
            @ApiResponse(responseCode = "404", description = "Área no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        if (!iAreaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Área no encontrada"));
        }
        iAreaRepository.deleteById(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(null, "Área eliminada correctamente"));
    }
}