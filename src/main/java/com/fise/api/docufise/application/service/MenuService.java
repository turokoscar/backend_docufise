package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.model.Rol;
import com.fise.api.docufise.domain.ports.input.IMenuInputPort;
import com.fise.api.docufise.domain.repository.IMenuRepository;
import com.fise.api.docufise.domain.repository.IRolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MenuService implements IMenuInputPort {
    
    private final IMenuRepository menuRepository;
    private final IRolRepository rolRepository;
    
    public MenuService(IMenuRepository menuRepository, IRolRepository rolRepository) {
        this.menuRepository = menuRepository;
        this.rolRepository = rolRepository;
    }
    
    @Override
    public List<Menu> listarTodos() {
        return menuRepository.findAll();
    }
    
    @Override
    public List<Menu> listarPorRol(Integer rolId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + rolId));
        return rol.getMenus() != null ? rol.getMenus() : Collections.emptyList();
    }
    
    @Override
    public Menu buscarPorId(Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Menú no encontrado con ID: " + id));
    }
    
    @Override
    public Menu crear(Menu menu) {
        return menuRepository.save(menu);
    }
    
    @Override
    public Menu actualizar(Integer id, Menu menu) {
        return menuRepository.findById(id)
                .map(existente -> {
                    menu.setId(id);
                    return menuRepository.save(menu);
                })
                .orElseThrow(() -> new NoSuchElementException("Menú no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Integer id) {
        if (!menuRepository.existsById(id)) {
            throw new NoSuchElementException("Menú no encontrado con ID: " + id);
        }
        menuRepository.deleteById(id);
    }
}
