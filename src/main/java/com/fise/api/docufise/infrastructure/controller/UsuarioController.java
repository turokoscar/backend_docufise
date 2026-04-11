package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import com.fise.api.docufise.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    private final IUsuarioRepository iUsuarioRepository;
    
    public UsuarioController(IUsuarioRepository iUsuarioRepository) {
        this.iUsuarioRepository = iUsuarioRepository;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarTodos() {
        List<Usuario> usuarios = iUsuarioRepository.findAll();
        return ResponseEntity.ok(ApiResponse.ok(usuarios, "Listado de usuarios", usuarios.size()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> buscarPorId(@PathVariable Integer id) {
        return iUsuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(ApiResponse.ok(usuario)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Usuario no encontrado")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(ApiResponse.ok(iUsuarioRepository.save(usuario)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return iUsuarioRepository.findById(id)
                .map(existente -> {
                    usuario.setId(id);
                    return ResponseEntity.ok(ApiResponse.ok(iUsuarioRepository.save(usuario)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Usuario no encontrado")));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        if (!iUsuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Usuario no encontrado"));
        }
        iUsuarioRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado correctamente"));
    }
}