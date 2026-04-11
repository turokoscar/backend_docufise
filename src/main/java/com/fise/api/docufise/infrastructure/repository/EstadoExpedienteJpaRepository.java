package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.EstadoExpedienteEntity;
import com.fise.api.docufise.domain.repository.IEstadoExpedienteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoExpedienteJpaRepository extends JpaRepository<EstadoExpedienteEntity, Integer>, IEstadoExpedienteRepository {
}
