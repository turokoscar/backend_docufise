package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Sesion;
import com.fise.api.docufise.domain.repository.ISesionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SesionJpaRepository extends JpaRepository<Sesion, Integer>, ISesionRepository {
    
    List<Sesion> findByUsuarioIdAndActivoTrue(Integer usuarioId);
    
    @Modifying
    @Query("UPDATE Sesion s SET s.activo = false WHERE s.id = :sessionId")
    void deactivateSessionById(Integer sessionId);
    
    @Override
    default void deactivateSession(Integer sessionId) {
        deactivateSessionById(sessionId);
    }
    
    @Modifying
    @Query("UPDATE Sesion s SET s.activo = false WHERE s.usuario.id = :usuarioId")
    void deactivateAllUserSessions(Integer usuarioId);
    
    @Override
    default void deactivateUserSessions(Integer usuarioId) {
        deactivateAllUserSessions(usuarioId);
    }
}