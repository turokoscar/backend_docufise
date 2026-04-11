package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.ports.input.IRolInputPort;
import com.fise.api.docufise.domain.repository.IRolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class RolService implements IRolInputPort {
    
    private final IRolRepository rolRepository;
    
    public RolService(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    
    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
    
    @Override
    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + id));
    }
    
    @Override
    public Rol crear(Rol rol) {
        return rolRepository.save(rol);
    }
    
    @Override
    public Rol actualizar(Integer id, Rol rol) {
        return rolRepository.findById(id)
                .map(existente -> {
                    rol.setId(id);
                    return rolRepository.save(rol);
                })
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new NoSuchElementException("Rol no encontrado con ID: " + id);
        }
        rolRepository.deleteById(id);
    }
}
