package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.shared.dto.FirmaRequest;
import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.domain.ports.input.FirmaInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/firmas")
@Tag(name = "Firmas", description = "Gestión de firmas digitales de documentos")
public class FirmaController {
    
    private final FirmaInputPort firmaInputPort;
    
    public FirmaController(FirmaInputPort firmaInputPort) {
        this.firmaInputPort = firmaInputPort;
    }
    
    @Operation(summary = "Crear solicitud de firma", description = "Crea una nueva solicitud de firma para un documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud de firma creada"),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado")
    })
    @PostMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Firma>> crear(@RequestBody FirmaRequest request) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaInputPort.crear(request)));
    }
    
    @Operation(summary = "Listar firmas por usuario", description = "Retorna todas las firmas asignadas a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de firmas")
    })
    @GetMapping
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Firma>>> listarPorUsuario(
            @Parameter(description = "ID del usuario", example = "1") @RequestParam Integer usuarioId) {
        List<Firma> lista = firmaInputPort.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(lista, "Listado de firmas por usuario", lista.size()));
    }
    
    @Operation(summary = "Listar firmas pendientes", description = "Retorna firmas pendientes de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de firmas pendientes")
    })
    @GetMapping("/pendientes")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<List<Firma>>> listarPendientes(
            @Parameter(example = "1") @RequestParam Integer usuarioId) {
        List<Firma> lista = firmaInputPort.listarPendientes(usuarioId);
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(lista, "Listado de firmas pendientes", lista.size()));
    }
    
    @Operation(summary = "Marcar como descargado", description = "Registra que el usuario descargó el documento para firmar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento marcado como descargado"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/descarga")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Firma>> marcarDescargado(
            @Parameter(example = "1") @PathVariable Integer id, 
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaInputPort.marcarDescargado(id, ip)));
    }
    
    @Operation(summary = "Firmar documento", description = "Registra la firma del documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento firmado"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/firmar")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Firma>> firmar(
            @Parameter(example = "1") @PathVariable Integer id,
            @Parameter(description = "Ruta del archivo firmado") @RequestParam String rutaArchivoFirmado,
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaInputPort.firmar(id, rutaArchivoFirmado, ip)));
    }
    
    @Operation(summary = "Rechazar firma", description = "Rechaza la firma con motivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firma rechazada"),
            @ApiResponse(responseCode = "404", description = "Firma no encontrada")
    })
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<com.fise.api.docufise.shared.dto.ApiResponse<Firma>> rechazar(
            @Parameter(example = "1") @PathVariable Integer id,
            @Parameter(description = "Motivo del rechazo") @RequestParam String motivoRechazo,
            @Parameter(description = "IP del cliente") @RequestParam String ip) {
        return ResponseEntity.ok(com.fise.api.docufise.shared.dto.ApiResponse.ok(firmaInputPort.rechazar(id, motivoRechazo, ip)));
    }
}