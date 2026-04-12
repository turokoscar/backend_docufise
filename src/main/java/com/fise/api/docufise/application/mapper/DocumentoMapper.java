package com.fise.api.docufise.application.mapper;

import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.shared.dto.DocumentoResponse;
import org.springframework.stereotype.Component;

@Component
public class DocumentoMapper {
    
    public DocumentoResponse toDto(Documento doc) {
        if (doc == null) return null;
        
        return DocumentoResponse.builder()
            .id(doc.getId())
            .numeracion(doc.getNumeracion())
            .tipoDocumento(doc.getTipoDocumento() != null ? doc.getTipoDocumento().getNombre() : null)
            .tipoDocumentoId(doc.getTipoDocumento() != null ? doc.getTipoDocumento().getId() : null)
            .usuarioElabora(doc.getUsuarioElabora() != null ? doc.getUsuarioElabora().getNombreCompleto() : null)
            .usuarioElaboraId(doc.getUsuarioElabora() != null ? doc.getUsuarioElabora().getId() : null)
            .usuarioEnvia(doc.getUsuarioEnvia() != null ? doc.getUsuarioEnvia().getNombreCompleto() : null)
            .usuarioEnviaId(doc.getUsuarioEnvia() != null ? doc.getUsuarioEnvia().getId() : null)
            .fechaElaboracion(doc.getFechaElaboracion())
            .fechaHoraEnvio(doc.getFechaHoraEnvio())
            .estado(doc.getEstado() != null ? doc.getEstado().getNombre() : null)
            .estadoId(doc.getEstado() != null ? doc.getEstado().getId() : null)
            .rutaArchivoOriginal(doc.getRutaArchivoOriginal())
            .rutaArchivoFirmado(doc.getRutaArchivoFirmado())
            .areaDestino(doc.getAreaDestino() != null ? doc.getAreaDestino().getNombre() : null)
            .areaDestinoId(doc.getAreaDestino() != null ? doc.getAreaDestino().getId() : null)
            .usuarioDestino(doc.getUsuarioDestino() != null ? doc.getUsuarioDestino().getNombreCompleto() : null)
            .usuarioDestinoId(doc.getUsuarioDestino() != null ? doc.getUsuarioDestino().getId() : null)
            .observaciones(doc.getObservaciones())
            .createdAt(doc.getCreatedAt())
            .updatedAt(doc.getUpdatedAt())
            .build();
    }
}