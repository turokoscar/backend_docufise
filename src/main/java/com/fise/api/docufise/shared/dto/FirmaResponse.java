package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirmaResponse {
    private Integer id;
    private Integer documentoId;
    private String documentoNumeracion;
    private String documentoTipoDocumento;
    private String documentoUsuarioElabora;
    private Integer usuarioAsignadoId;
    private String usuarioAsignadoNombre;
    private String estado;
    private Integer estadoId;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaDescarga;
    private LocalDateTime fechaFirma;
    private String rutaArchivoOriginal;
    private String rutaArchivoFirmado;
    private String motivoRechazo;
    private String ipDescarga;
    private String ipFirma;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}