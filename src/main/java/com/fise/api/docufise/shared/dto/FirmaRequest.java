package com.fise.api.docufise.shared.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class FirmaRequest {
    @NotNull(message = "El documento es requerido")
    private Integer documentoId;
    
    @NotNull(message = "El usuario asignado es requerido")
    private Integer usuarioAsignadoId;
    
    private String rutaArchivoOriginal;
}