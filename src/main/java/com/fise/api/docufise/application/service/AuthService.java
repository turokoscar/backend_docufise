package com.fise.api.docufise.application.service;

import com.fise.api.docufise.infrastructure.config.security.JwtTokenProvider;
import com.fise.api.docufise.shared.dto.LoginRequest;
import com.fise.api.docufise.shared.dto.LoginResponse;
import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {
    
    private final IUsuarioRepository IusuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    
    public AuthService(IUsuarioRepository IusuarioRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.IusuarioRepository = IusuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }
    
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = IusuarioRepository.findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasenaHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        String token = tokenProvider.generateToken(usuario.getNombreUsuario());
        
        usuario.setUltimoLogin(LocalDateTime.now());
        IusuarioRepository.save(usuario);
        
        return LoginResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .area(usuario.getArea() != null ? usuario.getArea().getNombre() : null)
                .build();
    }
}