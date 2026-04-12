package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.domain.repository.IFirmaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FirmaJpaRepository extends JpaRepository<Firma, Integer>, IFirmaRepository {
    
    @Query("SELECT f FROM Firma f LEFT JOIN FETCH f.usuarioAsignado LEFT JOIN FETCH f.documento td LEFT JOIN FETCH td.tipoDocumento LEFT JOIN FETCH td.usuarioElabora LEFT JOIN FETCH f.estado WHERE f.usuarioAsignado.id = :usuarioId")
    List<Firma> findByUsuarioAsignadoId(@Param("usuarioId") Integer usuarioId);
    
    @Query("SELECT f FROM Firma f LEFT JOIN FETCH f.usuarioAsignado LEFT JOIN FETCH f.documento td LEFT JOIN FETCH td.tipoDocumento LEFT JOIN FETCH td.usuarioElabora LEFT JOIN FETCH f.estado WHERE f.usuarioAsignado.id = :usuarioId AND f.estado.nombre = :estado")
    List<Firma> findByUsuarioAndEstado(@Param("usuarioId") Integer usuarioId, @Param("estado") String estado);
    
    @Query("SELECT f FROM Firma f LEFT JOIN FETCH f.usuarioAsignado LEFT JOIN FETCH f.documento td LEFT JOIN FETCH td.tipoDocumento LEFT JOIN FETCH td.usuarioElabora LEFT JOIN FETCH f.estado WHERE f.usuarioAsignado.id = :usuarioId AND f.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Firma> findPendientesByUsuario(@Param("usuarioId") Integer usuarioId);
    
    @Query("SELECT f FROM Firma f LEFT JOIN FETCH f.usuarioAsignado LEFT JOIN FETCH f.documento td LEFT JOIN FETCH td.tipoDocumento LEFT JOIN FETCH td.usuarioElabora LEFT JOIN FETCH td.estado LEFT JOIN FETCH f.estado WHERE f.id = :id")
    Optional<Firma> findByIdWithRelations(@Param("id") Integer id);
}
