package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaResponse {
    private int totalDocumentos;
    private int totalFirmados;
    private int totalPendientes;
    private int totalObservados;
    private int totalRegistrados;
    private int totalIngresados;
    private double tasaFirma;
    private List<EstadisticaMensual> tendenciaMensual;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstadisticaMensual {
        private String mes;
        private int cantidadDocumentos;
        private int cantidadFirmas;
    }
}
