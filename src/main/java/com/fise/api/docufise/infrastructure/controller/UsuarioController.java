package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.ports.input.IUsuarioInputPort;
import com.fise.api.docufise.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {
    
    private final IUsuarioInputPort usuarioInputPort;
    
    public UsuarioController(IUsuarioInputPort usuarioInputPort) {
        this.usuarioInputPort = usuarioInputPort;
    }
    
    @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios del sistema, opcionalmente filtrados por área")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarTodos(@RequestParam(required = false) Integer areaId) {
        List<Usuario> usuarios;
        if (areaId != null) {
            usuarios = usuarioInputPort.listarPorArea(areaId);
        } else {
            usuarios = usuarioInputPort.listarTodos();
        }
        return ResponseEntity.ok(ApiResponse.ok(usuarios, "Listado de usuarios", usuarios.size()));
    }
    
    @Operation(summary = "Buscar usuario", description = "Obtiene un usuario por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioInputPort.buscarPorId(id)));
    }
    
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioInputPort.crear(usuario)));
    }
    
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Usuario usuario) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioInputPort.actualizar(id, usuario)));
    }
    
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        usuarioInputPort.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado correctamente"));
    }
}