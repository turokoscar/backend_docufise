package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Rol;
import java.util.List;

public interface IRolInputPort {
    List<Rol> listarTodos();
    Rol buscarPorId(Integer id);
    Rol crear(Rol rol);
    Rol actualizar(Integer id, Rol rol);
    void eliminar(Integer id);
}
