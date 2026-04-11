package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Area;
import java.util.Optional;
import java.util.List;

public interface IAreaRepository {
    Optional<Area> findByNombre(String nombre);
    Area save(Area area);
    Optional<Area> findById(Integer id);
    List<Area> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}