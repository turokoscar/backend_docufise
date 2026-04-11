package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.exception.UsuarioNotFoundException;
import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.ports.input.IUsuarioInputPort;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UsuarioService implements IUsuarioInputPort {
    
    private final IUsuarioRepository usuarioRepository;
    
    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(id)));
    }
    
    @Override
    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario actualizar(Integer id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existente -> {
                    usuario.setId(id);
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(id)));
    }
    
    @Override
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(String.valueOf(id));
        }
        usuarioRepository.deleteById(id);
    }
}
