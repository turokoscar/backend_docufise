package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.ports.input.IMenuInputPort;
import com.fise.api.docufise.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "Menús", description = "Gestión de menús de navegación")
public class MenuController {
    
    private final IMenuInputPort menuInputPort;
    
    public MenuController(IMenuInputPort menuInputPort) {
        this.menuInputPort = menuInputPort;
    }
    
    @Operation(summary = "Listar menús", description = "Retorna todos los menús del sistema")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> listarTodos() {
        List<Menu> menus = menuInputPort.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(menus, "Listado de menús", menus.size()));
    }
    
    @Operation(summary = "Listar menús por rol", description = "Retorna los menús asignados a un rol específico")
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<ApiResponse<List<Menu>>> listarPorRol(@Parameter(example = "1") @PathVariable Integer rolId) {
        List<Menu> menus = menuInputPort.listarPorRol(rolId);
        return ResponseEntity.ok(ApiResponse.ok(menus, "Menús del rol", menus.size()));
    }
    
    @Operation(summary = "Buscar menú", description = "Obtiene un menú por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(menuInputPort.buscarPorId(id)));
    }
    
    @Operation(summary = "Crear menú", description = "Registra un nuevo menú")
    @PostMapping
    public ResponseEntity<ApiResponse<Menu>> crear(@RequestBody Menu menu) {
        return ResponseEntity.ok(ApiResponse.ok(menuInputPort.crear(menu)));
    }
    
    @Operation(summary = "Actualizar menú", description = "Actualiza los datos de un menú")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Menu>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Menu menu) {
        return ResponseEntity.ok(ApiResponse.ok(menuInputPort.actualizar(id, menu)));
    }
    
    @Operation(summary = "Eliminar menú", description = "Elimina un menú")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        menuInputPort.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Menú eliminado correctamente"));
    }
}