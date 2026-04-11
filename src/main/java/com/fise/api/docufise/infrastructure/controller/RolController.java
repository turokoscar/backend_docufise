package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.repository.IRolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Gestión de roles del sistema")
public class RolController {
    
    private final IRolRepository rolRepository;
    
    public RolController(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    
    @Operation(summary = "Listar roles", description = "Retorna todos los roles del sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de roles")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Rol>>> listar() {
        List<Rol> roles = rolRepository.findAll();
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(roles, "Listado de roles"));
    }
    
    @Operation(summary = "Buscar rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Rol>> buscarPorId(@PathVariable Integer id) {
        return rolRepository.findById(id)
                .map(rol -> ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(rol)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Rol no encontrado")));
    }
    
    @Operation(summary = "Crear rol")
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Rol>> crear(@RequestBody Rol rol) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(rolRepository.save(rol)));
    }
    
    @Operation(summary = "Actualizar rol")
    @PutMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Rol>> actualizar(
            @PathVariable Integer id, 
            @RequestBody Rol rol) {
        return rolRepository.findById(id)
                .map(existente -> {
                    rol.setId(id);
                    return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(rolRepository.save(rol)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Rol no encontrado")));
    }
    
    @Operation(summary = "Eliminar rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        if (!rolRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Rol no encontrado"));
        }
        rolRepository.deleteById(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(null, "Rol eliminado"));
    }
}