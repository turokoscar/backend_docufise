package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SeguridadService {
    
    private final IUsuarioRepository usuarioRepository;
    
    @Value("${security.login.max-attempts}")
    private int maxIntentosFallidos;
    
    @Value("${security.login.lockout-minutes}")
    private int minutosBloqueo;
    
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
        
        if (intentos >= maxIntentosFallidos) {
            usuario.setBloqueoHasta(LocalDateTime.now().plusMinutes(minutosBloqueo));
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
    
    public int getMaxIntentosFallidos() {
        return maxIntentosFallidos;
    }
    
    public int getMinutosBloqueo() {
        return minutosBloqueo;
    }
}