package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.shared.dto.FirmaRequest;
import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.application.service.FirmaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/firmas")
public class FirmaController {
    
    private final FirmaService firmaService;
    
    public FirmaController(FirmaService firmaService) {
        this.firmaService = firmaService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Firma>> crear(@RequestBody FirmaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(firmaService.crear(request)));
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Firma>>> listarPorUsuario(@PathVariable Integer usuarioId) {
        List<Firma> lista = firmaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.ok(lista, "Listado de firmas por usuario", lista.size()));
    }
    
    @GetMapping("/pendientes/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Firma>>> listarPendientes(@PathVariable Integer usuarioId) {
        List<Firma> lista = firmaService.listarPendientes(usuarioId);
        return ResponseEntity.ok(ApiResponse.ok(lista, "Listado de firmas pendientes", lista.size()));
    }
    
    @PutMapping("/{id}/descargado")
    public ResponseEntity<ApiResponse<Firma>> marcarDescargado(@PathVariable Integer id, 
                                             @RequestParam String ip) {
        return ResponseEntity.ok(ApiResponse.ok(firmaService.marcarDescargado(id, ip)));
    }
    
    @PutMapping("/{id}/firmar")
    public ResponseEntity<ApiResponse<Firma>> firmar(@PathVariable Integer id,
                                     @RequestParam String rutaArchivoFirmado,
                                     @RequestParam String ip) {
        return ResponseEntity.ok(ApiResponse.ok(firmaService.firmar(id, rutaArchivoFirmado, ip)));
    }
    
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ApiResponse<Firma>> rechazar(@PathVariable Integer id,
                                     @RequestParam String motivoRechazo,
                                     @RequestParam String ip) {
        return ResponseEntity.ok(ApiResponse.ok(firmaService.rechazar(id, motivoRechazo, ip)));
    }
}