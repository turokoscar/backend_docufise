package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.DocumentoRequest;
import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.domain.ports.input.DocumentoInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documentos")
@Tag(name = "Documentos", description = "Gestión de documentos y expedientes")
public class DocumentoController {
    
    private final DocumentoInputPort documentoInputPort;
    
    public DocumentoController(DocumentoInputPort documentoInputPort) {
        this.documentoInputPort = documentoInputPort;
    }
    
    @Operation(summary = "Crear documento", description = "Registra un nuevo documento con estado inicial REGISTRADO")
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Documento>> crear(@RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoInputPort.crear(request)));
    }
    
    @Operation(summary = "Actualizar documento", description = "Actualiza un documento existente")
    @PutMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Documento>> actualizar(@Parameter(example = "1") @PathVariable Integer id, @RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoInputPort.actualizar(id, request)));
    }
    
    @Operation(summary = "Eliminar documento", description = "Elimina un documento por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        documentoInputPort.eliminar(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(null, "Documento eliminado correctamente"));
    }
    
    @Operation(summary = "Buscar documento", description = "Obtiene un documento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Documento>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoInputPort.buscarPorId(id)));
    }
    
    @Operation(summary = "Listar documentos", description = "Lista documentos con filtros")
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Documento>>> listarTodos(
            @Parameter(description = "Estado: REGISTRADO, INGRESADO, PENDIENTE, OBSERVADO, FIRMADO", example = "PENDIENTE")
            @RequestParam(required = false) String estado,
            @Parameter(example = "1") @RequestParam(required = false) Integer areaId,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer usuarioElaboraId,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        List<Documento> lista;
        
        if (estado != null && areaId != null) {
            lista = documentoInputPort.listarPendientesPorArea(areaId);
        } else if (estado != null && usuarioId != null) {
            lista = documentoInputPort.listarPendientesPorUsuario(usuarioId);
        } else if (usuarioElaboraId != null) {
            lista = documentoInputPort.listarPorUsuarioElabora(usuarioElaboraId);
        } else {
            lista = documentoInputPort.listarTodos();
        }
        
        int total = lista.size();
        if (page != null && limit != null) {
            int start = (page - 1) * limit;
            lista = lista.stream().skip(start).limit(limit).collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(lista, "Listado de documentos", total));
    }
    
    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un documento")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Documento>> cambiarEstado(@Parameter(example = "1") @PathVariable Integer id, 
            @RequestParam Integer estadoId,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoInputPort.cambiarEstado(id, estadoId, observaciones)));
    }
    
    @Operation(summary = "Derivar documento", description = "Deriva un documento a otra área o usuario")
    @PatchMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Documento>> derivar(@Parameter(example = "1") @PathVariable Integer id,
            @RequestParam(required = false) Integer areaDestinoId,
            @RequestParam(required = false) Integer usuarioDestinoId) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoInputPort.derivar(id, areaDestinoId, usuarioDestinoId)));
    }
}