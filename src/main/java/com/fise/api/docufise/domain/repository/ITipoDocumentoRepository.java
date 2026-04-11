package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.TipoDocumento;
import java.util.Optional;
import java.util.List;

public interface ITipoDocumentoRepository {
    Optional<TipoDocumento> findByNombre(String nombre);
    TipoDocumento save(TipoDocumento tipo);
    Optional<TipoDocumento> findById(Integer id);
    List<TipoDocumento> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}