package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Menu;
import java.util.List;

public interface IMenuInputPort {
    List<Menu> listarTodos();
    Menu buscarPorId(Integer id);
    Menu crear(Menu menu);
    Menu actualizar(Integer id, Menu menu);
    void eliminar(Integer id);
}
