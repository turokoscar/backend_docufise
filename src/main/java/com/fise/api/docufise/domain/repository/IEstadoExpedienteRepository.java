package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.EstadoExpedienteEntity;
import java.util.Optional;
import java.util.List;

public interface IEstadoExpedienteRepository {
    Optional<EstadoExpedienteEntity> findByNombre(String nombre);
    EstadoExpedienteEntity save(EstadoExpedienteEntity estado);
    Optional<EstadoExpedienteEntity> findById(Integer id);
    List<EstadoExpedienteEntity> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}