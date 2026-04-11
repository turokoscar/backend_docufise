package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.domain.model.Firma;
import com.fise.api.docufise.shared.dto.FirmaRequest;
import java.util.List;

public interface FirmaInputPort {
    
    Firma crear(FirmaRequest request);
    
    Firma marcarDescargado(Integer id, String ip);
    
    Firma firmar(Integer id, String rutaArchivoFirmado, String ip);
    
    Firma rechazar(Integer id, String motivoRechazo, String ip);
    
    List<Firma> listarPorUsuario(Integer usuarioId);
    
    List<Firma> listarPendientes(Integer usuarioId);
}