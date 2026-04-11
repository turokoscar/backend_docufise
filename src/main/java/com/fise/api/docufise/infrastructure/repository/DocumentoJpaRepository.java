package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.domain.repository.IDocumentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentoJpaRepository extends JpaRepository<Documento, Integer>, IDocumentoRepository {
    @Override
    @Query("SELECT d FROM Documento d WHERE d.estado.nombre = :estado")
    List<Documento> findByEstadoNombre(@Param("estado") String estado);
    @Override
    @Query("SELECT d FROM Documento d WHERE d.areaDestino.id = :areaId AND d.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Documento> findPendientesByArea(@Param("areaId") Integer areaId);
    @Override
    @Query("SELECT d FROM Documento d WHERE d.usuarioDestino.id = :usuarioId AND d.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Documento> findPendientesByUsuario(@Param("usuarioId") Integer usuarioId);
}
