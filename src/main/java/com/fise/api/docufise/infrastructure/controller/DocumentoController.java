package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.DocumentoRequest;
import com.fise.api.docufise.shared.dto.DocumentoResponse;
import com.fise.api.docufise.shared.dto.EstadisticaResponse;
import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.domain.ports.input.DocumentoInputPort;
import com.fise.api.docufise.application.service.FileStorageService;
import com.fise.api.docufise.application.mapper.DocumentoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documentos")
@Tag(name = "Documentos", description = "Gestión de documentos y expedientes")
public class DocumentoController {
    
    private final DocumentoInputPort documentoInputPort;
    private final FileStorageService fileStorageService;
    private final DocumentoMapper documentoMapper;
    
    public DocumentoController(DocumentoInputPort documentoInputPort, FileStorageService fileStorageService, DocumentoMapper documentoMapper) {
        this.documentoInputPort = documentoInputPort;
        this.fileStorageService = fileStorageService;
        this.documentoMapper = documentoMapper;
    }
    
    @Operation(summary = "Crear documento", description = "Registra un nuevo documento con estado inicial REGISTRADO")
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<DocumentoResponse>> crear(
            @RequestPart("documento") DocumentoRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        
        if (archivo != null && !archivo.isEmpty()) {
            try {
                String filename = fileStorageService.upload(archivo, "documentos");
                request.setRutaArchivoOriginal(filename);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                    com.fise.api.docufise.shared.dto.ApiResponse.error("Error al guardar archivo: " + e.getMessage()));
            }
        }
        
        Documento doc = documentoInputPort.crear(request);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoMapper.toDto(doc)));
    }
    
    @Operation(summary = "Actualizar documento", description = "Actualiza un documento existente")
    @PutMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<DocumentoResponse>> actualizar(
            @Parameter(example = "1") @PathVariable Integer id, 
            @RequestPart("documento") DocumentoRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        
        if (archivo != null && !archivo.isEmpty()) {
            try {
                if (request.getRutaArchivoOriginal() != null && !request.getRutaArchivoOriginal().isBlank()) {
                    fileStorageService.replace(request.getRutaArchivoOriginal(), archivo);
                } else {
                    String filename = fileStorageService.upload(archivo, "documentos");
                    request.setRutaArchivoOriginal(filename);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                    com.fise.api.docufise.shared.dto.ApiResponse.error("Error al guardar archivo: " + e.getMessage()));
            }
        }
        
        Documento doc = documentoInputPort.actualizar(id, request);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoMapper.toDto(doc)));
    }
    
    @Operation(summary = "Eliminar documento", description = "Elimina un documento por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Void>> eliminar(@Parameter(example = "1") @PathVariable Integer id) {
        documentoInputPort.eliminar(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(null, "Documento eliminado correctamente"));
    }
    
    @Operation(summary = "Buscar documento", description = "Obtiene un documento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<DocumentoResponse>> buscarPorId(@Parameter(example = "1") @PathVariable Integer id) {
        Documento doc = documentoInputPort.buscarPorId(id);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoMapper.toDto(doc)));
    }
    
    @Operation(summary = "Listar documentos", description = "Lista documentos con filtros")
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<DocumentoResponse>>> listarTodos(
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
        
        List<DocumentoResponse> listaDto = lista.stream()
            .map(documentoMapper::toDto)
            .collect(Collectors.toList());
        
        int total = listaDto.size();
        if (page != null && limit != null) {
            int start = (page - 1) * limit;
            listaDto = listaDto.stream().skip(start).limit(limit).collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(listaDto, "Listado de documentos", total));
    }
    
    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un documento")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<DocumentoResponse>> cambiarEstado(@Parameter(example = "1") @PathVariable Integer id, 
            @RequestParam Integer estadoId,
            @RequestParam(required = false) String observaciones) {
        Documento doc = documentoInputPort.cambiarEstado(id, estadoId, observaciones);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoMapper.toDto(doc)));
    }
    
    @Operation(summary = "Derivar documento", description = "Deriva un documento a otra área o usuario")
    @PatchMapping("/{id}/derivar")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<DocumentoResponse>> derivar(@Parameter(example = "1") @PathVariable Integer id,
            @RequestParam(required = false) Integer areaDestinoId,
            @RequestParam(required = false) Integer usuarioDestinoId,
            @RequestParam(required = false) Integer usuarioEnviaId) {
        Documento doc = documentoInputPort.derivar(id, areaDestinoId, usuarioDestinoId, usuarioEnviaId);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(documentoMapper.toDto(doc)));
    }
    
    @Operation(summary = "Estadísticas generales", description = "Obtiene estadísticas y tendencia mensual de documentos y firmas")
    @GetMapping("/estadisticas")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<EstadisticaResponse>> getEstadisticas() {
        EstadisticaResponse estadisticas = documentoInputPort.getEstadisticas();
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(estadisticas));
    }
}