package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.domain.model.Area;
import com.fise.api.docufise.domain.repository.IAreaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {
    
    private final IAreaRepository iAreaRepository;
    
    public AreaController(IAreaRepository iAreaRepository) {
        this.iAreaRepository = iAreaRepository;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Area>>> listarTodas() {
        List<Area> lista = iAreaRepository.findAll();
        return ResponseEntity.ok(ApiResponse.ok(lista, "Listado de áreas", lista.size()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> buscarPorId(@PathVariable Integer id) {
        return iAreaRepository.findById(id)
                .map(area -> ResponseEntity.ok(ApiResponse.ok(area)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Área no encontrada")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Area>> crear(@RequestBody Area area) {
        return ResponseEntity.ok(ApiResponse.ok(iAreaRepository.save(area)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> actualizar(@PathVariable Integer id, @RequestBody Area area) {
        return iAreaRepository.findById(id)
                .map(existente -> {
                    area.setId(id);
                    return ResponseEntity.ok(ApiResponse.ok(iAreaRepository.save(area)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Área no encontrada")));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        if (!iAreaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Área no encontrada"));
        }
        iAreaRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Área eliminada correctamente"));
    }
}