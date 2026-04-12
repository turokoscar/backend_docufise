package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.domain.repository.IDocumentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoJpaRepository extends JpaRepository<Documento, Integer>, IDocumentoRepository {
    
    Optional<Documento> findByNumeracion(String numeracion);
    boolean existsByNumeracion(String numeracion);
    
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.tipoDocumento LEFT JOIN FETCH d.usuarioElabora LEFT JOIN FETCH d.usuarioEnvia LEFT JOIN FETCH d.estado LEFT JOIN FETCH d.areaDestino LEFT JOIN FETCH d.usuarioDestino")
    List<Documento> findAllWithRelations();
    
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.tipoDocumento LEFT JOIN FETCH d.usuarioElabora LEFT JOIN FETCH d.usuarioEnvia LEFT JOIN FETCH d.estado LEFT JOIN FETCH d.areaDestino LEFT JOIN FETCH d.usuarioDestino WHERE d.id = :id")
    Optional<Documento> findByIdWithRelations(@Param("id") Integer id);
    
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.tipoDocumento LEFT JOIN FETCH d.usuarioElabora LEFT JOIN FETCH d.usuarioEnvia LEFT JOIN FETCH d.estado LEFT JOIN FETCH d.areaDestino LEFT JOIN FETCH d.usuarioDestino WHERE d.usuarioElabora.id = :usuarioId")
    List<Documento> findByUsuarioElaboraId(@Param("usuarioId") Integer usuarioId);
    
    List<Documento> findByUsuarioEnviaId(Integer usuarioId);
    List<Documento> findByUsuarioDestinoId(Integer usuarioId);
    List<Documento> findByAreaDestinoId(Integer areaId);
    List<Documento> findByFechaElaboracionBetween(LocalDate inicio, LocalDate fin);
    
    @Query("SELECT d FROM Documento d WHERE d.estado.nombre = :estado")
    List<Documento> findByEstadoNombre(@Param("estado") String estado);
    
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.tipoDocumento LEFT JOIN FETCH d.usuarioElabora LEFT JOIN FETCH d.usuarioEnvia LEFT JOIN FETCH d.estado LEFT JOIN FETCH d.areaDestino LEFT JOIN FETCH d.usuarioDestino WHERE d.areaDestino.id = :areaId AND d.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Documento> findPendientesByArea(@Param("areaId") Integer areaId);
    
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.tipoDocumento LEFT JOIN FETCH d.usuarioElabora LEFT JOIN FETCH d.usuarioEnvia LEFT JOIN FETCH d.estado LEFT JOIN FETCH d.areaDestino LEFT JOIN FETCH d.usuarioDestino WHERE d.usuarioDestino.id = :usuarioId AND d.estado.nombre IN ('PENDIENTE', 'OBSERVADO')")
    List<Documento> findPendientesByUsuario(@Param("usuarioId") Integer usuarioId);
}