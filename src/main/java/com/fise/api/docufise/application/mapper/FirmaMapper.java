package com.fise.api.docufise.application.mapper;

import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.shared.dto.FirmaResponse;
import org.springframework.stereotype.Component;

@Component
public class FirmaMapper {
    
    public FirmaResponse toDto(Firma firma) {
        if (firma == null) return null;
        
        return FirmaResponse.builder()
            .id(firma.getId())
            .documentoId(firma.getDocumento() != null ? firma.getDocumento().getId() : null)
            .documentoNumeracion(firma.getDocumento() != null ? firma.getDocumento().getNumeracion() : null)
            .documentoTipoDocumento(firma.getDocumento() != null && firma.getDocumento().getTipoDocumento() != null 
                ? firma.getDocumento().getTipoDocumento().getNombre() : null)
            .documentoUsuarioElabora(firma.getDocumento() != null && firma.getDocumento().getUsuarioElabora() != null
                ? firma.getDocumento().getUsuarioElabora().getNombreCompleto() : null)
            .usuarioAsignadoId(firma.getUsuarioAsignado() != null ? firma.getUsuarioAsignado().getId() : null)
            .usuarioAsignadoNombre(firma.getUsuarioAsignado() != null ? firma.getUsuarioAsignado().getNombreCompleto() : null)
            .estado(firma.getEstado() != null ? firma.getEstado().getNombre() : null)
            .estadoId(firma.getEstado() != null ? firma.getEstado().getId() : null)
            .fechaAsignacion(firma.getFechaAsignacion())
            .fechaDescarga(firma.getFechaDescarga())
            .fechaFirma(firma.getFechaFirma())
            .rutaArchivoOriginal(firma.getRutaArchivoOriginal())
            .rutaArchivoFirmado(firma.getRutaArchivoFirmado())
            .motivoRechazo(firma.getMotivoRechazo())
            .ipDescarga(firma.getIpDescarga())
            .ipFirma(firma.getIpFirma())
            .createdAt(firma.getCreatedAt())
            .updatedAt(firma.getUpdatedAt())
            .build();
    }
}