package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Area;
import com.fise.api.docufise.domain.ports.input.IAreaInputPort;
import com.fise.api.docufise.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/areas")
@Tag(name = "Áreas", description = "Gestión de áreas organizacionales")
public class AreaController {
    
    private final IAreaInputPort areaInputPort;
    
    public AreaController(IAreaInputPort areaInputPort) {
        this.areaInputPort = areaInputPort;
    }
    
    @Operation(summary = "Listar áreas", description = "Retorna todas las áreas registradas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Area>>> listarTodas() {
        List<Area> lista = areaInputPort.listarTodas();
        return ResponseEntity.ok(ApiResponse.ok(lista, "Listado de áreas", lista.size()));
    }
    
    @Operation(summary = "Buscar área", description = "Obtiene un área por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(areaInputPort.buscarPorId(id)));
    }
    
    @Operation(summary = "Crear área", description = "Registra una nueva área")
    @PostMapping
    public ResponseEntity<ApiResponse<Area>> crear(@RequestBody Area area) {
        return ResponseEntity.ok(ApiResponse.ok(areaInputPort.crear(area)));
    }
    
    @Operation(summary = "Actualizar área", description = "Actualiza los datos de un área")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Area area) {
        return ResponseEntity.ok(ApiResponse.ok(areaInputPort.actualizar(id, area)));
    }
    
    @Operation(summary = "Eliminar área", description = "Elimina un área")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        areaInputPort.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Área eliminada correctamente"));
    }
}