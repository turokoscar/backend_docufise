package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.model.RolMenu;
import com.fise.api.docufise.domain.repository.IMenuRepository;
import com.fise.api.docufise.domain.repository.IRolMenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "Menús", description = "Gestión de menús del sistema")
public class MenuController {
    
    private final IMenuRepository menuRepository;
    private final IRolMenuRepository rolMenuRepository;
    
    public MenuController(IMenuRepository menuRepository, IRolMenuRepository rolMenuRepository) {
        this.menuRepository = menuRepository;
        this.rolMenuRepository = rolMenuRepository;
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
    
    @Operation(summary = "Listar menús por rol", description = "Retorna los menús asignados a un rol específico")
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Menu>>> listarPorRol(@PathVariable Integer rolId) {
        List<RolMenu> rolMenus = rolMenuRepository.findByRolId(rolId);
        List<Integer> menuIds = rolMenus.stream()
                .map(RolMenu::getMenuId)
                .collect(Collectors.toList());
        
        List<Menu> menus = menuRepository.findAll().stream()
                .filter(m -> menuIds.contains(m.getId()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(menus, "Menús del rol"));
    }
}