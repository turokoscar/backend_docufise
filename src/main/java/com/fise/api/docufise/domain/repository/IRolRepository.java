package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Rol;
import java.util.Optional;
import java.util.List;

public interface IRolRepository {
    Optional<Rol> findByNombre(String nombre);
    Rol save(Rol rol);
    Optional<Rol> findById(Integer id);
    List<Rol> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
    List<Object[]> findRolMenusWithMenuByRolId(Integer rolId);
}