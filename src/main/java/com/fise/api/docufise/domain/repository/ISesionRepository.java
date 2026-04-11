package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Sesion;
import java.util.List;

public interface ISesionRepository {
    Sesion save(Sesion sesion);
    List<Sesion> findByUsuarioIdAndActivoTrue(Integer usuarioId);
    void deactivateSession(Integer sessionId);
    void deactivateUserSessions(Integer usuarioId);
}