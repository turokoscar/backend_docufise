package com.fise.api.docufise.shared.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DocumentoRequest {
    @NotBlank(message = "La numeración es requerida")
    private String numeracion;
    
    @NotNull(message = "El tipo de documento es requerido")
    private Integer tipoDocumentoId;
    
    @NotNull(message = "El usuario que elabora es requerido")
    private Integer usuarioElaboraId;
    
    private Integer usuarioEnviaId;
    
    @NotNull(message = "La fecha de elaboración es requerida")
    private LocalDate fechaElaboracion;
    
    private String fechaHoraEnvio;
    
    @NotNull(message = "El estado es requerido")
    private Integer estadoId;
    
    private String rutaArchivoOriginal;
    
    private Integer areaDestinoId;
    
    private Integer usuarioDestinoId;
    
    private String observaciones;
}