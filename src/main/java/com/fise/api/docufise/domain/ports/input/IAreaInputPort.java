package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Area;
import java.util.List;

public interface IAreaInputPort {
    List<Area> listarTodas();
    Area buscarPorId(Integer id);
    Area crear(Area area);
    Area actualizar(Integer id, Area area);
    void eliminar(Integer id);
}
