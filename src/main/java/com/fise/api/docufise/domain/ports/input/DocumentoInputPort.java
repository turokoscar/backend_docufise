package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Documento;
import com.fise.api.docufise.shared.dto.DocumentoRequest;
import java.util.List;

public interface DocumentoInputPort {
    
    Documento crear(DocumentoRequest request);
    
    Documento actualizar(Integer id, DocumentoRequest request);
    
    void eliminar(Integer id);
    
    Documento buscarPorId(Integer id);
    
    List<Documento> listarTodos();
    
    List<Documento> listarPorUsuarioElabora(Integer usuarioId);
    
    List<Documento> listarPendientesPorArea(Integer areaId);
    
    List<Documento> listarPendientesPorUsuario(Integer usuarioId);
    
    Documento cambiarEstado(Integer id, Integer estadoId, String observaciones);
    
    Documento derivar(Integer id, Integer areaDestinoId, Integer usuarioDestinoId);
}