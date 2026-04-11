package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Integer>, IUsuarioRepository {
}
