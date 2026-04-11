package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.DocumentoHistorial;
import com.fise.api.docufise.domain.repository.IHistorialRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialJpaRepository extends JpaRepository<DocumentoHistorial, Integer>, IHistorialRepository {
    
    List<DocumentoHistorial> findByDocumentoIdOrderByCreatedAtDesc(Integer documentoId);
    
    default List<DocumentoHistorial> findByDocumentoId(Integer documentoId) {
        return findByDocumentoIdOrderByCreatedAtDesc(documentoId);
    }
    
    default List<DocumentoHistorial> findTopByDocumentoIdOrderByCreatedAtDesc(Integer documentoId) {
        return findByDocumentoIdOrderByCreatedAtDesc(documentoId);
    }
}