package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Documento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDocumentoRepository {
    Optional<Documento> findByNumeracion(String numeracion);
    boolean existsByNumeracion(String numeracion);
    List<Documento> findByUsuarioElaboraId(Integer usuarioId);
    List<Documento> findByUsuarioEnviaId(Integer usuarioId);
    List<Documento> findByUsuarioDestinoId(Integer usuarioId);
    List<Documento> findByAreaDestinoId(Integer areaId);
    List<Documento> findByEstadoNombre(String estado);
    List<Documento> findPendientesByArea(Integer areaId);
    List<Documento> findPendientesByUsuario(Integer usuarioId);
    List<Documento> findByFechaElaboracionBetween(LocalDate inicio, LocalDate fin);
    Documento save(Documento documento);
    Optional<Documento> findById(Integer id);
    Optional<Documento> findByIdWithRelations(Integer id);
    List<Documento> findAll();
    List<Documento> findAllWithRelations();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}