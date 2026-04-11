package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.shared.dto.DocumentoRequest;
import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.application.service.DocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {
    
    private final DocumentoService documentoService;
    
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Documento>> crear(@RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(documentoService.crear(request)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Documento>> actualizar(@PathVariable Integer id, @RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(documentoService.actualizar(id, request)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        documentoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Documento eliminado correctamente"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Documento>> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(documentoService.buscarPorId(id)));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Documento>>> listarTodos() {
        List<Documento> lista = documentoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(lista, "Listado de documentos", lista.size()));
    }
    
    @GetMapping("/elabora/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Documento>>> listarPorUsuarioElabora(@PathVariable Integer usuarioId) {
        List<Documento> lista = documentoService.listarPorUsuarioElabora(usuarioId);
        return ResponseEntity.ok(ApiResponse.ok(lista, "Documentos por usuario elaborador", lista.size()));
    }
    
    @GetMapping("/pendientes/area/{areaId}")
    public ResponseEntity<ApiResponse<List<Documento>>> listarPendientesPorArea(@PathVariable Integer areaId) {
        List<Documento> lista = documentoService.listarPendientesPorArea(areaId);
        return ResponseEntity.ok(ApiResponse.ok(lista, "Documentos pendientes por área", lista.size()));
    }
    
    @GetMapping("/pendientes/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Documento>>> listarPendientesPorUsuario(@PathVariable Integer usuarioId) {
        List<Documento> lista = documentoService.listarPendientesPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.ok(lista, "Documentos pendientes por usuario", lista.size()));
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Documento>> cambiarEstado(@PathVariable Integer id, 
                                                @RequestParam Integer estadoId,
                                                @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(ApiResponse.ok(documentoService.cambiarEstado(id, estadoId, observaciones)));
    }
    
    @PutMapping("/{id}/derivar")
    public ResponseEntity<ApiResponse<Documento>> derivar(@PathVariable Integer id,
                                            @RequestParam(required = false) Integer areaDestinoId,
                                            @RequestParam(required = false) Integer usuarioDestinoId) {
        return ResponseEntity.ok(ApiResponse.ok(documentoService.derivar(id, areaDestinoId, usuarioDestinoId)));
    }
}