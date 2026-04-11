package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.repository.IMenuRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Integer>, IMenuRepository {
}
