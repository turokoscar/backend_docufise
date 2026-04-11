package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.DocumentoHistorial;
import java.util.List;

public interface IHistorialRepository {
    DocumentoHistorial save(DocumentoHistorial historial);
    List<DocumentoHistorial> findByDocumentoId(Integer documentoId);
    List<DocumentoHistorial> findTopByDocumentoIdOrderByCreatedAtDesc(Integer documentoId);
}