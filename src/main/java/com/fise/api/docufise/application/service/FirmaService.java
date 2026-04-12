package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.exception.DocumentoNotFoundException;
import com.fise.api.docufise.domain.exception.EstadoNotFoundException;
import com.fise.api.docufise.domain.exception.FirmaNotFoundException;
import com.fise.api.docufise.domain.exception.UsuarioNotFoundException;
import com.fise.api.docufise.domain.ports.input.FirmaInputPort;
import com.fise.api.docufise.shared.dto.FirmaRequest;
import com.fise.api.docufise.domain.model.*;
import com.fise.api.docufise.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FirmaService implements FirmaInputPort {
    
    private final IFirmaRepository firmaRepository;
    private final IDocumentoRepository documentoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IEstadoExpedienteRepository estadoExpedienteRepository;
    
    public FirmaService(IFirmaRepository firmaRepository,
                     IDocumentoRepository documentoRepository,
                     IUsuarioRepository usuarioRepository,
                     IEstadoExpedienteRepository estadoExpedienteRepository) {
        this.firmaRepository = firmaRepository;
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoExpedienteRepository = estadoExpedienteRepository;
    }
    
    @Override
    public Firma crear(FirmaRequest request) {
        Documento documento = documentoRepository.findById(request.getDocumentoId())
                .orElseThrow(() -> new DocumentoNotFoundException(request.getDocumentoId()));
        
        Usuario usuarioAsignado = usuarioRepository.findById(request.getUsuarioAsignadoId())
                .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(request.getUsuarioAsignadoId())));
        
        EstadoExpedienteEntity estado = estadoExpedienteRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new EstadoNotFoundException(0));
        
        Firma firma = Firma.builder()
                .documento(documento)
                .usuarioAsignado(usuarioAsignado)
                .estado(estado)
                .rutaArchivoOriginal(request.getRutaArchivoOriginal())
                .build();
        
        return firmaRepository.save(firma);
    }
    
    @Override
    public Firma marcarDescargado(Integer id, String ip) {
        Firma firma = firmaRepository.findById(id)
                .orElseThrow(() -> new FirmaNotFoundException(id));
        
        firma.setFechaDescarga(LocalDateTime.now());
        firma.setIpDescarga(ip);
        
        return firmaRepository.save(firma);
    }
    
    @Override
    public Firma firmar(Integer id, String rutaArchivoFirmado, String ip) {
        Firma firma = firmaRepository.findById(id)
                .orElseThrow(() -> new FirmaNotFoundException(id));
        
        EstadoExpedienteEntity estado = estadoExpedienteRepository.findByNombre("FIRMADO")
                .orElseThrow(() -> new EstadoNotFoundException(0));
        
        firma.setFechaFirma(LocalDateTime.now());
        firma.setRutaArchivoFirmado(rutaArchivoFirmado);
        firma.setIpFirma(ip);
        firma.setEstado(estado);
        
        return firmaRepository.save(firma);
    }
    
    @Override
    public Firma rechazar(Integer id, String motivoRechazo, String ip) {
        Firma firma = firmaRepository.findById(id)
                .orElseThrow(() -> new FirmaNotFoundException(id));
        
        EstadoExpedienteEntity estado = estadoExpedienteRepository.findByNombre("OBSERVADO")
                .orElseThrow(() -> new EstadoNotFoundException(0));
        
        firma.setMotivoRechazo(motivoRechazo);
        firma.setIpFirma(ip);
        firma.setEstado(estado);
        
        return firmaRepository.save(firma);
    }
    
    @Override
    public List<Firma> listarPorUsuario(Integer usuarioId) {
        return firmaRepository.findByUsuarioAsignadoId(usuarioId);
    }
    
    @Override
    public List<Firma> listarPendientes(Integer usuarioId) {
        return firmaRepository.findPendientesByUsuario(usuarioId);
    }

    @Override
    public Firma buscarPorId(Integer id) {
        return firmaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new FirmaNotFoundException(id));
    }
}