package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SeguridadService {
    
    private final IUsuarioRepository usuarioRepository;
    
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int MINUTOS_BLOQUEO = 15;
    
    public SeguridadService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public record ResultadoLogin(boolean exitoso, String mensaje) {}
    
    public ResultadoLogin validarLogin(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .map(usuario -> {
                    if (!usuario.getActivo()) {
                        return new ResultadoLogin(false, "Usuario inactivo");
                    }
                    
                    if (usuario.getBloqueoHasta() != null) {
                        if (usuario.getBloqueoHasta().isAfter(LocalDateTime.now())) {
                            long minutosRestantes = java.time.Duration.between(
                                    LocalDateTime.now(), 
                                    usuario.getBloqueoHasta()
                            ).toMinutes();
                            return new ResultadoLogin(false, "Usuario bloqueado. Intente en " + minutosRestantes + " minutos");
                        } else {
                            desbloquear(usuario);
                        }
                    }
                    
                    return new ResultadoLogin(true, "OK");
                })
                .orElse(new ResultadoLogin(false, "Usuario no encontrado"));
    }
    
    public void registrarIntentoFallido(Usuario usuario) {
        int intentos = usuario.getIntentosFallo() != null ? usuario.getIntentosFallo() + 1 : 1;
        usuario.setIntentosFallo(intentos);
        
        if (intentos >= MAX_INTENTOS_FALLIDOS) {
            usuario.setBloqueoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
        }
        
        usuarioRepository.save(usuario);
    }
    
    public void registrarIntentoExitoso(Usuario usuario) {
        usuario.setIntentosFallo(0);
        usuario.setBloqueoHasta(null);
        usuarioRepository.save(usuario);
    }
    
    private void desbloquear(Usuario usuario) {
        usuario.setIntentosFallo(0);
        usuario.setBloqueoHasta(null);
        usuarioRepository.save(usuario);
    }
    
    public void desbloquearSiEstaBloqueado(Usuario usuario) {
        if (usuario.getBloqueoHasta() != null && usuario.getBloqueoHasta().isBefore(LocalDateTime.now())) {
            desbloquear(usuario);
        }
    }
    
    public static int getMaxIntentosFallidos() {
        return MAX_INTENTOS_FALLIDOS;
    }
    
    public static int getMinutosBloqueo() {
        return MINUTOS_BLOQUEO;
    }
}