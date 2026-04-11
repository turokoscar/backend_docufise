package com.fise.api.docufise.application.service;

import com.fise.api.docufise.shared.dto.DocumentoRequest;
import com.fise.api.docufise.shared.dto.DocumentoResponse;
import com.fise.api.docufise.domain.model.*;
import com.fise.api.docufise.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentoService {
    
    private final IDocumentoRepository IdocumentoRepository;
    private final IUsuarioRepository IusuarioRepository;
    private final ITipoDocumentoRepository ItipoDocumentoRepository;
    private final IEstadoExpedienteRepository IestadoExpedienteRepository;
    private final IAreaRepository IareaRepository;
    
    public DocumentoService(IDocumentoRepository IdocumentoRepository,
                        IUsuarioRepository IusuarioRepository,
                        ITipoDocumentoRepository ItipoDocumentoRepository,
                        IEstadoExpedienteRepository IestadoExpedienteRepository,
                        IAreaRepository IareaRepository) {
        this.IdocumentoRepository = IdocumentoRepository;
        this.IusuarioRepository = IusuarioRepository;
        this.ItipoDocumentoRepository = ItipoDocumentoRepository;
        this.IestadoExpedienteRepository = IestadoExpedienteRepository;
        this.IareaRepository = IareaRepository;
    }
    
    public Documento crear(DocumentoRequest request) {
        TipoDocumento tipoDocumento = ItipoDocumentoRepository.findById(request.getTipoDocumentoId())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
        
        Usuario usuarioElabora = IusuarioRepository.findById(request.getUsuarioElaboraId())
                .orElseThrow(() -> new RuntimeException("Usuario elabora no encontrado"));
        
        EstadoExpedienteEntity estado = IestadoExpedienteRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        
        Documento documento = Documento.builder()
                .numeracion(request.getNumeracion())
                .tipoDocumento(tipoDocumento)
                .usuarioElabora(usuarioElabora)
                .fechaElaboracion(request.getFechaElaboracion())
                .estado(estado)
                .rutaArchivoOriginal(request.getRutaArchivoOriginal())
                .observaciones(request.getObservaciones())
                .build();
        
        if (request.getUsuarioEnviaId() != null) {
            documento.setUsuarioEnvia(IusuarioRepository.findById(request.getUsuarioEnviaId()).orElse(null));
        }
        
        if (request.getAreaDestinoId() != null) {
            documento.setAreaDestino(IareaRepository.findById(request.getAreaDestinoId()).orElse(null));
        }
        
        if (request.getUsuarioDestinoId() != null) {
            documento.setUsuarioDestino(IusuarioRepository.findById(request.getUsuarioDestinoId()).orElse(null));
        }
        
        documento.setFechaHoraEnvio(request.getFechaHoraEnvio());
        
        return IdocumentoRepository.save(documento);
    }
    
    public Documento actualizar(Integer id, DocumentoRequest request) {
        Documento documento = IdocumentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        
        documento.setNumeracion(request.getNumeracion());
        documento.setObservaciones(request.getObservaciones());
        documento.setRutaArchivoOriginal(request.getRutaArchivoOriginal());
        
        return IdocumentoRepository.save(documento);
    }
    
    public void eliminar(Integer id) {
        IdocumentoRepository.deleteById(id);
    }
    
    public Documento buscarPorId(Integer id) {
        return IdocumentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }
    
    public List<Documento> listarTodos() {
        return IdocumentoRepository.findAll();
    }
    
    public List<Documento> listarPorUsuarioElabora(Integer usuarioId) {
        return IdocumentoRepository.findByUsuarioElaboraId(usuarioId);
    }
    
    public List<Documento> listarPendientesPorArea(Integer areaId) {
        return IdocumentoRepository.findPendientesByArea(areaId);
    }
    
    public List<Documento> listarPendientesPorUsuario(Integer usuarioId) {
        return IdocumentoRepository.findPendientesByUsuario(usuarioId);
    }
    
    public Documento cambiarEstado(Integer id, Integer estadoId, String observaciones) {
        Documento documento = IdocumentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        
        EstadoExpedienteEntity estado = IestadoExpedienteRepository.findById(estadoId)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        
        documento.setEstado(estado);
        if (observaciones != null) {
            documento.setObservaciones(observaciones);
        }
        
        return IdocumentoRepository.save(documento);
    }
    
    public Documento derivar(Integer id, Integer areaDestinoId, Integer usuarioDestinoId) {
        Documento documento = IdocumentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        
        if (areaDestinoId != null) {
            documento.setAreaDestino(IareaRepository.findById(areaDestinoId).orElse(null));
        }
        
        if (usuarioDestinoId != null) {
            documento.setUsuarioDestino(IusuarioRepository.findById(usuarioDestinoId).orElse(null));
        }
        
        documento.setFechaHoraEnvio(LocalDateTime.now().toString());
        
        return IdocumentoRepository.save(documento);
    }
}