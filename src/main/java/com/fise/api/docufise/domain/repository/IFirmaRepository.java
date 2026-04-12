package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Firma;
import java.util.List;
import java.util.Optional;

public interface IFirmaRepository {
    List<Firma> findByDocumentoId(Integer documentoId);
    List<Firma> findByUsuarioAsignadoId(Integer usuarioId);
    List<Firma> findByUsuarioAndEstado(Integer usuarioId, String estado);
    Optional<Firma> findByDocumentoIdAndUsuarioAsignadoId(Integer documentoId, Integer usuarioId);
    List<Firma> findPendientesByUsuario(Integer usuarioId);
    Optional<Firma> findByIdWithRelations(Integer id);
    Firma save(Firma firma);
    Optional<Firma> findById(Integer id);
    List<Firma> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}