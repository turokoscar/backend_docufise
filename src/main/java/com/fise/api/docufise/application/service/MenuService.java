package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Menu;
import com.fise.api.docufise.domain.ports.input.IMenuInputPort;
import com.fise.api.docufise.domain.repository.IMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MenuService implements IMenuInputPort {
    
    private final IMenuRepository menuRepository;
    
    public MenuService(IMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    
    @Override
    public List<Menu> listarTodos() {
        return menuRepository.findAll();
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
