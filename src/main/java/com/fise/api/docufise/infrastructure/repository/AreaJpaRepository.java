package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Area;
import com.fise.api.docufise.domain.repository.IAreaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaJpaRepository extends JpaRepository<Area, Integer>, IAreaRepository {
}
