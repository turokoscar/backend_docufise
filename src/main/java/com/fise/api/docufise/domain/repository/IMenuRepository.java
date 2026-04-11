package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Menu;
import java.util.List;
import java.util.Optional;

public interface IMenuRepository {
    List<Menu> findByActivoTrueOrderByOrdenAsc();
    Menu save(Menu menu);
    Optional<Menu> findById(Integer id);
    List<Menu> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}