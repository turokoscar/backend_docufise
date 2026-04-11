package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoResponse {
    private Integer id;
    private String numeracion;
    private String tipoDocumento;
    private Integer tipoDocumentoId;
    private String usuarioElabora;
    private Integer usuarioElaboraId;
    private String usuarioEnvia;
    private Integer usuarioEnviaId;
    private LocalDate fechaElaboracion;
    private String fechaHoraEnvio;
    private String estado;
    private Integer estadoId;
    private String colorHex;
    private String rutaArchivoOriginal;
    private String rutaArchivoFirmado;
    private String areaDestino;
    private Integer areaDestinoId;
    private String usuarioDestino;
    private Integer usuarioDestinoId;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}