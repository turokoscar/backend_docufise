package com.fise.api.docufise.application.service;

import com.fise.api.docufise.shared.dto.FirmaRequest;
import com.fise.api.docufise.domain.model.*;
import com.fise.api.docufise.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FirmaService {
    
    private final IFirmaRepository IfirmaRepository;
    private final IDocumentoRepository IdocumentoRepository;
    private final IUsuarioRepository IusuarioRepository;
    private final IEstadoExpedienteRepository IestadoExpedienteRepository;
    
    public FirmaService(IFirmaRepository IfirmaRepository,
                     IDocumentoRepository IdocumentoRepository,
                     IUsuarioRepository IusuarioRepository,
                     IEstadoExpedienteRepository IestadoExpedienteRepository) {
        this.IfirmaRepository = IfirmaRepository;
        this.IdocumentoRepository = IdocumentoRepository;
        this.IusuarioRepository = IusuarioRepository;
        this.IestadoExpedienteRepository = IestadoExpedienteRepository;
    }
    
    public Firma crear(FirmaRequest request) {
        Documento documento = IdocumentoRepository.findById(request.getDocumentoId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        
        Usuario usuarioAsignado = IusuarioRepository.findById(request.getUsuarioAsignadoId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        EstadoExpedienteEntity estado = IestadoExpedienteRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));
        
        Firma firma = Firma.builder()
                .documento(documento)
                .usuarioAsignado(usuarioAsignado)
                .estado(estado)
                .rutaArchivoOriginal(request.getRutaArchivoOriginal())
                .build();
        
        return IfirmaRepository.save(firma);
    }
    
    public Firma marcarDescargado(Integer id, String ip) {
        Firma firma = IfirmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Firma no encontrada"));
        
        firma.setFechaDescarga(LocalDateTime.now());
        firma.setIpDescarga(ip);
        
        return IfirmaRepository.save(firma);
    }
    
    public Firma firmar(Integer id, String rutaArchivoFirmado, String ip) {
        Firma firma = IfirmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Firma no encontrada"));
        
        EstadoExpedienteEntity estado = IestadoExpedienteRepository.findByNombre("FIRMADO")
                .orElseThrow(() -> new RuntimeException("Estado FIRMADO no encontrado"));
        
        firma.setFechaFirma(LocalDateTime.now());
        firma.setRutaArchivoFirmado(rutaArchivoFirmado);
        firma.setIpFirma(ip);
        firma.setEstado(estado);
        
        return IfirmaRepository.save(firma);
    }
    
    public Firma rechazar(Integer id, String motivoRechazo, String ip) {
        Firma firma = IfirmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Firma no encontrada"));
        
        EstadoExpedienteEntity estado = IestadoExpedienteRepository.findByNombre("OBSERVADO")
                .orElseThrow(() -> new RuntimeException("Estado OBSERVADO no encontrado"));
        
        firma.setMotivoRechazo(motivoRechazo);
        firma.setIpFirma(ip);
        firma.setEstado(estado);
        
        return IfirmaRepository.save(firma);
    }
    
    public List<Firma> listarPorUsuario(Integer usuarioId) {
        return IfirmaRepository.findByUsuarioAsignadoId(usuarioId);
    }
    
    public List<Firma> listarPendientes(Integer usuarioId) {
        return IfirmaRepository.findPendientesByUsuario(usuarioId);
    }
}