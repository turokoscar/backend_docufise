package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Integer id;
    private String nombre;
    private String ruta;
    private String icono;
    private Integer orden;
    private Boolean activo;
    private String permiso;
}