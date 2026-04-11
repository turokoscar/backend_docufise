package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.domain.repository.IFirmaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FirmaJpaRepository extends JpaRepository<Firma, Integer>, IFirmaRepository {
    @Override
    @Query("SELECT f FROM Firma f WHERE f.usuarioAsignado.id = :usuarioId AND f.estado.nombre = :estado")
    List<Firma> findByUsuarioAndEstado(@Param("usuarioId") Integer usuarioId, @Param("estado") String estado);
    @Override
    @Query("SELECT f FROM Firma f WHERE f.usuarioAsignado.id = :usuarioId AND f.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Firma> findPendientesByUsuario(@Param("usuarioId") Integer usuarioId);
}
