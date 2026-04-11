package com.fise.api.docufise.domain.repository;

import com.fise.api.docufise.domain.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface IUsuarioRepository {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreo(String correo);
    
    // Métodos estándar requeridos por los servicios
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Integer id);
    List<Usuario> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}