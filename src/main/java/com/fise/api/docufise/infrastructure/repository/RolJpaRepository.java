package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.repository.IRolRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolJpaRepository extends JpaRepository<Rol, Integer>, IRolRepository {
}
