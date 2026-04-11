package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.ports.input.IRolInputPort;
import com.fise.api.docufise.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Gestión de roles de usuario")
public class RolController {
    
    private final IRolInputPort rolInputPort;
    
    public RolController(IRolInputPort rolInputPort) {
        this.rolInputPort = rolInputPort;
    }
    
    @Operation(summary = "Listar roles", description = "Retorna todos los roles del sistema")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Rol>>> listarTodos() {
        List<Rol> roles = rolInputPort.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(roles, "Listado de roles", roles.size()));
    }
    
    @Operation(summary = "Buscar rol", description = "Obtiene un rol por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(rolInputPort.buscarPorId(id)));
    }
    
    @Operation(summary = "Crear rol", description = "Registra un nuevo rol")
    @PostMapping
    public ResponseEntity<ApiResponse<Rol>> crear(@RequestBody Rol rol) {
        return ResponseEntity.ok(ApiResponse.ok(rolInputPort.crear(rol)));
    }
    
    @Operation(summary = "Actualizar rol", description = "Actualiza los datos de un rol")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Rol rol) {
        return ResponseEntity.ok(ApiResponse.ok(rolInputPort.actualizar(id, rol)));
    }
    
    @Operation(summary = "Eliminar rol", description = "Elimina un rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        rolInputPort.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Rol eliminado correctamente"));
    }
}