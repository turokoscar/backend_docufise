package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.repository.IMenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "Menús", description = "Gestión de menús del sistema")
public class MenuController {
    
    private final IMenuRepository menuRepository;
    
    public MenuController(IMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    
    @Operation(summary = "Listar menús", description = "Retorna todos los menús del sistema")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de menús")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Menu>>> listar() {
        List<Menu> menus = menuRepository.findAll();
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(menus, "Listado de menús"));
    }
    
    @Operation(summary = "Buscar menú por ID")
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Menu>> buscarPorId(@PathVariable Integer id) {
        return menuRepository.findById(id)
                .map(menu -> ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(menu)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.fise.api.docufise.shared.dto.ApiResponse.notFound("Menú no encontrado")));
    }
}