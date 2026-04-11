package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.RolMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IRolMenuRepository extends JpaRepository<RolMenu, Integer> {
    List<RolMenu> findByRolId(Integer rolId);
}