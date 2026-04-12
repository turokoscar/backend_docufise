package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.exception.DocumentoNotFoundException;
import com.fise.api.docufise.domain.exception.EstadoNotFoundException;
import com.fise.api.docufise.domain.exception.TipoDocumentoNotFoundException;
import com.fise.api.docufise.domain.exception.UsuarioNotFoundException;
import com.fise.api.docufise.domain.ports.input.DocumentoInputPort;
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
public class DocumentoService implements DocumentoInputPort {
    
    private final IDocumentoRepository documentoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ITipoDocumentoRepository tipoDocumentoRepository;
    private final IEstadoExpedienteRepository estadoExpedienteRepository;
    private final IAreaRepository areaRepository;
    private final IHistorialRepository historialRepository;
    private final IFirmaRepository firmaRepository;
    
    public DocumentoService(IDocumentoRepository documentoRepository,
                        IUsuarioRepository usuarioRepository,
                        ITipoDocumentoRepository tipoDocumentoRepository,
                        IEstadoExpedienteRepository estadoExpedienteRepository,
                        IAreaRepository areaRepository,
                        IHistorialRepository historialRepository,
                        IFirmaRepository firmaRepository) {
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoExpedienteRepository = estadoExpedienteRepository;
        this.areaRepository = areaRepository;
        this.historialRepository = historialRepository;
        this.firmaRepository = firmaRepository;
    }
    
    @Override
    public Documento crear(DocumentoRequest request) {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                .orElseThrow(() -> new TipoDocumentoNotFoundException(request.getTipoDocumentoId()));
        
        Usuario usuarioElabora = usuarioRepository.findById(request.getUsuarioElaboraId())
                .orElseThrow(() -> new UsuarioNotFoundException(String.valueOf(request.getUsuarioElaboraId())));
        
        EstadoExpedienteEntity estado = estadoExpedienteRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EstadoNotFoundException(request.getEstadoId()));
        
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
            documento.setUsuarioEnvia(usuarioRepository.findById(request.getUsuarioEnviaId()).orElse(null));
        }
        
        if (request.getAreaDestinoId() != null) {
            documento.setAreaDestino(areaRepository.findById(request.getAreaDestinoId()).orElse(null));
        }
        
        if (request.getUsuarioDestinoId() != null) {
            documento.setUsuarioDestino(usuarioRepository.findById(request.getUsuarioDestinoId()).orElse(null));
        }
        
        documento.setFechaHoraEnvio(request.getFechaHoraEnvio());
        
        return documentoRepository.save(documento);
    }
    
    @Override
    public Documento actualizar(Integer id, DocumentoRequest request) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException(id));
        
        documento.setNumeracion(request.getNumeracion());
        documento.setObservaciones(request.getObservaciones());
        documento.setRutaArchivoOriginal(request.getRutaArchivoOriginal());
        
        return documentoRepository.save(documento);
    }
    
    @Override
    public void eliminar(Integer id) {
        documentoRepository.deleteById(id);
    }
    
    @Override
    public Documento buscarPorId(Integer id) {
        return documentoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new DocumentoNotFoundException(id));
    }
    
    @Override
    public List<Documento> listarTodos() {
        return documentoRepository.findAllWithRelations();
    }
    
    @Override
    public List<Documento> listarPorUsuarioElabora(Integer usuarioId) {
        return documentoRepository.findByUsuarioElaboraId(usuarioId);
    }
    
    @Override
    public List<Documento> listarPendientesPorArea(Integer areaId) {
        return documentoRepository.findPendientesByArea(areaId);
    }
    
    @Override
    public List<Documento> listarPendientesPorUsuario(Integer usuarioId) {
        return documentoRepository.findPendientesByUsuario(usuarioId);
    }
    
    @Override
    public Documento cambiarEstado(Integer id, Integer estadoId, String observaciones) {
        Documento documento = documentoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new DocumentoNotFoundException(id));
        
        Integer estadoAnteriorId = documento.getEstado() != null ? documento.getEstado().getId() : null;
        
        EstadoExpedienteEntity estado = estadoExpedienteRepository.findById(estadoId)
                .orElseThrow(() -> new EstadoNotFoundException(estadoId));
        
        documento.setEstado(estado);
        if (observaciones != null) {
            documento.setObservaciones(observaciones);
        }
        
        Documento documentoGuardado = documentoRepository.save(documento);
        
        if (estadoAnteriorId != null) {
            DocumentoHistorial historial = DocumentoHistorial.builder()
                    .documentoId(id)
                    .estadoAnteriorId(estadoAnteriorId)
                    .estadoNuevoId(estadoId)
                    .usuarioCambiaId(documento.getUsuarioElabora() != null ? documento.getUsuarioElabora().getId() : 0)
                    .motivoCambio(observaciones)
                    .build();
            historialRepository.save(historial);
        }
        
        return documentoGuardado;
    }
    
    @Override
    public Documento derivar(Integer id, Integer areaDestinoId, Integer usuarioDestinoId, Integer usuarioEnviaId) {
        Documento documento = documentoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new DocumentoNotFoundException(id));
        
        Integer estadoAnteriorId = documento.getEstado() != null ? documento.getEstado().getId() : null;
        
        if (areaDestinoId != null) {
            documento.setAreaDestino(areaRepository.findById(areaDestinoId).orElse(null));
        }
        
        if (usuarioDestinoId != null) {
            documento.setUsuarioDestino(usuarioRepository.findById(usuarioDestinoId).orElse(null));
        }

        if (usuarioEnviaId != null) {
            documento.setUsuarioEnvia(usuarioRepository.findById(usuarioEnviaId).orElse(null));
        }
        
        // Formatear fecha como yyyy-MM-dd HH:mm
        LocalDateTime now = LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        documento.setFechaHoraEnvio(now.format(formatter));
        
        EstadoExpedienteEntity estadoIngresado = estadoExpedienteRepository.findByNombre("INGRESADO").orElse(null);
        if (estadoIngresado != null) {
            documento.setEstado(estadoIngresado);
        }

        Documento documentoGuardado = documentoRepository.save(documento);
        
        if (estadoIngresado != null && estadoAnteriorId != null) {
            DocumentoHistorial historial = DocumentoHistorial.builder()
                    .documentoId(id)
                    .estadoAnteriorId(estadoAnteriorId)
                    .estadoNuevoId(estadoIngresado.getId())
                    .usuarioCambiaId(usuarioEnviaId != null ? usuarioEnviaId : 0)
                    .motivoCambio("Derivación a área/usuario destino")
                    .build();
            historialRepository.save(historial);
        }

        // Crear registro de Firma si se derivó a un usuario específico
        if (usuarioDestinoId != null && estadoIngresado != null) {
            Usuario usuarioAsignado = usuarioRepository.findById(usuarioDestinoId).orElse(null);
            if (usuarioAsignado != null) {
                Firma firma = Firma.builder()
                        .documento(documentoGuardado)
                        .usuarioAsignado(usuarioAsignado)
                        .estado(estadoIngresado)
                        .rutaArchivoOriginal(documentoGuardado.getRutaArchivoOriginal())
                        .build();
                firmaRepository.save(firma);
            }
        }
        
        return documentoGuardado;
    }
}