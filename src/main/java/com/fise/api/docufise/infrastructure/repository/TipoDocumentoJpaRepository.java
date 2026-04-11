package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.TipoDocumento;
import com.fise.api.docufise.domain.repository.ITipoDocumentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoJpaRepository extends JpaRepository<TipoDocumento, Integer>, ITipoDocumentoRepository {
}
