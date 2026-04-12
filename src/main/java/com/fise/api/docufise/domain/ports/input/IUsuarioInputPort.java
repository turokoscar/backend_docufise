package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Usuario;
import java.util.List;

public interface IUsuarioInputPort {
    List<Usuario> listarTodos();
    List<Usuario> listarPorArea(Integer areaId);
    Usuario buscarPorId(Integer id);
    Usuario crear(Usuario usuario);
    Usuario actualizar(Integer id, Usuario usuario);
    void eliminar(Integer id);
}
