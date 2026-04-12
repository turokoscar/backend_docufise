package com.fise.api.docufise.infrastructure.controller;

import com.fise.api.docufise.application.service.FileStorageService;
import com.fise.api.docufise.shared.dto.ApiResponse;
import com.fise.api.docufise.shared.dto.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@Tag(name = "Archivos", description = "Gestión de almacenamiento de archivos")
public class FileController {
    
    private final FileStorageService fileStorageService;
    
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    
    @Operation(summary = "Subir archivo", description = "Sube un nuevo archivo al almacenamiento")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileResponse>> upload(
            @Parameter(description = "Archivo a subir") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Subcarpeta (opcional)") @RequestParam(required = false) String subfolder) {
        
        try {
            String filepath = fileStorageService.upload(file, subfolder);
            FileResponse response = FileResponse.builder()
                    .filename(file.getOriginalFilename())
                    .filepath(filepath)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .message("Archivo subido correctamente")
                    .build();
            
            return ResponseEntity.ok(ApiResponse.ok(response, "Archivo subido", 1));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), 400));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al subir archivo: " + e.getMessage(), 500));
        }
    }
    
    @Operation(summary = "Reemplazar archivo", description = "Reemplaza un archivo existente")
    @PutMapping("/replace/{filename:.+}")
    public ResponseEntity<ApiResponse<FileResponse>> replace(
            @Parameter(description = "Nombre del archivo a reemplazar") @PathVariable String filename,
            @Parameter(description = "Nuevo archivo") @RequestParam("file") MultipartFile file) {
        
        try {
            boolean success = fileStorageService.replace(filename, file);
            if (success) {
                FileResponse response = FileResponse.builder()
                        .filename(filename)
                        .filepath(fileStorageService.getFullPath(filename))
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .message("Archivo reemplazado correctamente")
                        .build();
                
                return ResponseEntity.ok(ApiResponse.ok(response, "Archivo reemplazado", 1));
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), 400));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al reemplazar archivo: " + e.getMessage(), 500));
        }
    }
    
    @Operation(summary = "Eliminar archivo", description = "Elimina un archivo del almacenamiento")
    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Nombre del archivo a eliminar") @PathVariable String filename) {
        
        boolean success = fileStorageService.delete(filename);
        if (success) {
            return ResponseEntity.ok(ApiResponse.ok(null, "Archivo eliminado correctamente"));
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Descargar archivo", description = "Descarga un archivo del almacenamiento")
    @GetMapping("/{filename:.+}")
    public ResponseEntity<InputStreamResource> download(
            @Parameter(description = "Nombre del archivo a descargar") @PathVariable String filename) {
        
        try {
            if (!fileStorageService.exists(filename)) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] bytes = fileStorageService.getBytes(filename);
            String contentType = "application/octet-stream";
            
            if (filename.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.toLowerCase().endsWith(".doc")) {
                contentType = "application/msword";
            } else if (filename.toLowerCase().endsWith(".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (filename.toLowerCase().endsWith(".xls")) {
                contentType = "application/vnd.ms-excel";
            } else if (filename.toLowerCase().endsWith(".xlsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (filename.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(new InputStreamResource(new java.io.ByteArrayInputStream(bytes)));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}