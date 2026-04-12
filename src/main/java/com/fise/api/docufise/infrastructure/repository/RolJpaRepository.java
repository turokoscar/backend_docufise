package com.fise.api.docufise.infrastructure.repository;

import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.repository.IRolRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RolJpaRepository extends JpaRepository<Rol, Integer>, IRolRepository {
    
    @Query("SELECT rm.ide_rol, rm.ide_menu, rm.permiso, m FROM RolMenu rm JOIN Menu m ON rm.ide_menu = m.id WHERE rm.ide_rol = :rolId")
    List<Object[]> findRolMenusWithMenuByRolId(Integer rolId);
}
