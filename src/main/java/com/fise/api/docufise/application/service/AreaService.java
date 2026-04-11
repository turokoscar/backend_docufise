package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Area;
import com.fise.api.docufise.domain.ports.input.IAreaInputPort;
import com.fise.api.docufise.domain.repository.IAreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AreaService implements IAreaInputPort {
    
    private final IAreaRepository areaRepository;
    
    public AreaService(IAreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }
    
    @Override
    public List<Area> listarTodas() {
        return areaRepository.findAll();
    }
    
    @Override
    public Area buscarPorId(Integer id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Área no encontrada con ID: " + id));
    }
    
    @Override
    public Area crear(Area area) {
        return areaRepository.save(area);
    }
    
    @Override
    public Area actualizar(Integer id, Area area) {
        return areaRepository.findById(id)
                .map(existente -> {
                    area.setId(id);
                    return areaRepository.save(area);
                })
                .orElseThrow(() -> new NoSuchElementException("Área no encontrada con ID: " + id));
    }
    
    @Override
    public void eliminar(Integer id) {
        if (!areaRepository.existsById(id)) {
            throw new NoSuchElementException("Área no encontrada con ID: " + id);
        }
        areaRepository.deleteById(id);
    }
}
