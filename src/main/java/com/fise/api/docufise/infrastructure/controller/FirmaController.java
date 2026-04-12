package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.FirmaRequest;
import com.fise.api.docufise.shared.dto.FirmaResponse;
import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.domain.ports.input.FirmaInputPort;
import com.fise.api.docufise.application.service.FileStorageService;
import com.fise.api.docufise.application.mapper.FirmaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/firmas")
@Tag(name = "Firmas", description = "Gestión de firmas digitales de documentos")
public class FirmaController {
    
    private final FirmaInputPort firmaInputPort;
    private final FileStorageService fileStorageService;
    private final FirmaMapper firmaMapper;
    
    public FirmaController(FirmaInputPort firmaInputPort, FileStorageService fileStorageService, FirmaMapper firmaMapper) {
        this.firmaInputPort = firmaInputPort;
        this.fileStorageService = fileStorageService;
        this.firmaMapper = firmaMapper;
    }
    
    @Operation(summary = "Crear solicitud de firma", description = "Crea una nueva solicitud de firma para un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud de firma creada"),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado")
    })
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<FirmaResponse>> crear(@RequestBody FirmaRequest request) {
        Firma firma = firmaInputPort.crear(request);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaMapper.toDto(firma)));
    }
    
    @Operation(summary = "Listar firmas por usuario", description = "Retorna todas las firmas asignadas a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de firmas")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<FirmaResponse>>> listarPorUsuario(
            @Parameter(description = "ID del usuario asignado", example = "1") @RequestParam Integer usuarioAsignadoId) {
        List<Firma> lista = firmaInputPort.listarPorUsuario(usuarioAsignadoId);
        List<FirmaResponse> listaDto = lista.stream().map(firmaMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(listaDto, "Listado de firmas por usuario", listaDto.size()));
    }
    
    @Operation(summary = "Listar firmas pendientes", description = "Retorna firmas pendientes de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de firmas pendientes")
    })
    @GetMapping("/pendientes")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<FirmaResponse>>> listarPendientes(
            @Parameter(example = "1") @RequestParam Integer usuarioAsignadoId) {
        List<Firma> lista = firmaInputPort.listarPendientes(usuarioAsignadoId);
        List<FirmaResponse> listaDto = lista.stream().map(firmaMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(listaDto, "Listado de firmas pendientes", listaDto.size()));
    }
    
    @Operation(summary = "Marcar como descargado", description = "Registra que el usuario descargó el documento para firmar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento marcado como descargado"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/descarga")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<FirmaResponse>> marcarDescargado(
            @Parameter(example = "1") @PathVariable Integer id, 
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        Firma firma = firmaInputPort.marcarDescargado(id, ip);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaMapper.toDto(firma)));
    }
    
    @Operation(summary = "Firmar documento", description = "Registra la firma del documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento firmado"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/firmar")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<FirmaResponse>> firmar(
            @Parameter(example = "1") @PathVariable Integer id,
            @RequestPart("archivoFirmado") MultipartFile archivoFirmado,
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        
        String rutaArchivoFirmado;
        try {
            rutaArchivoFirmado = fileStorageService.upload(archivoFirmado, "firmados");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                com.fise.api.docufise.shared.dto.ApiResponse.error("Error al guardar archivo firmado: " + e.getMessage()));
        }
        
        Firma firma = firmaInputPort.firmar(id, rutaArchivoFirmado, ip);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaMapper.toDto(firma)));
    }
    
    @Operation(summary = "Rechazar firma", description = "Rechaza la firma con motivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firma rechazada"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<FirmaResponse>> rechazar(
            @Parameter(example = "1") @PathVariable Integer id,
            @Parameter(description = "Motivo del rechazo") @RequestParam String motivoRechazo,
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        Firma firma = firmaInputPort.rechazar(id, motivoRechazo, ip);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaMapper.toDto(firma)));
    }
    
    @Operation(summary = "Descargar documento original", description = "Descargar el documento a firmar")
    @GetMapping("/{id}/descargar-archivo")
    public ResponseEntity<byte[]> descargarDocumento(@Parameter(example = "1") @PathVariable Integer id) {
        Firma firma = firmaInputPort.buscarPorId(id);
        if (firma.getDocumento() != null && firma.getDocumento().getRutaArchivoOriginal() != null) {
            byte[] bytes = fileStorageService.getBytes(firma.getDocumento().getRutaArchivoOriginal());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"documento.pdf\"")
                    .header("Content-Type", "application/pdf")
                    .body(bytes);
        }
        return ResponseEntity.notFound().build();
    }
}