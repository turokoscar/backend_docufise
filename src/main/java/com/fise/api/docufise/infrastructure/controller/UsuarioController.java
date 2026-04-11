package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {
    
    private final IUsuarioRepository iUsuarioRepository;
    
    public UsuarioController(IUsuarioRepository iUsuarioRepository) {
        this.iUsuarioRepository = iUsuarioRepository;
    }
    
    @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Usuario>>> listarTodos() {
        List<Usuario> usuarios = iUsuarioRepository.findAll();
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(usuarios, "Listado de usuarios", usuarios.size()));
    }
    
    @Operation(summary = "Buscar usuario", description = "Obtiene un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Usuario>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return iUsuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(usuario)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Usuario no encontrado")));
    }
    
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
    })
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Usuario>> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(iUsuarioRepository.save(usuario)));
    }
    
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Usuario>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestBody Usuario usuario) {
        return iUsuarioRepository.findById(id)
                .map(existente -> {
                    usuario.setId(id);
                    return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(iUsuarioRepository.save(usuario)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Usuario no encontrado")));
    }
    
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        if (!iUsuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Usuario no encontrado"));
        }
        iUsuarioRepository.deleteById(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(null, "Usuario eliminado correctamente"));
    }
}