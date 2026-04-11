package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.exception.InvalidCredentialsException;
import com.fise.api.docufise.domain.exception.MaximosIntentosExcedidosException;
import com.fise.api.docufise.domain.exception.UsuarioBloqueadoException;
import com.fise.api.docufise.domain.exception.UsuarioInactivoException;
import com.fise.api.docufise.domain.exception.UsuarioNotFoundException;
import com.fise.api.docufise.domain.model.TokenBlacklist;
import com.fise.api.docufise.domain.model.Usuario;
import com.fise.api.docufise.domain.ports.input.AuthInputPort;
import com.fise.api.docufise.domain.ports.output.IJwtTokenProvider;
import com.fise.api.docufise.domain.repository.ITokenBlacklistRepository;
import com.fise.api.docufise.domain.repository.ISesionRepository;
import com.fise.api.docufise.domain.repository.IUsuarioRepository;
import com.fise.api.docufise.shared.dto.LoginRequest;
import com.fise.api.docufise.shared.dto.LoginResponse;
import com.fise.api.docufise.shared.dto.SesionResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService implements AuthInputPort {
    
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtTokenProvider tokenProvider;
    private final ITokenBlacklistRepository tokenBlacklistRepository;
    private final ISesionRepository sesionRepository;
    private final SeguridadService seguridadService;
    
    public AuthService(IUsuarioRepository usuarioRepository, 
                      PasswordEncoder passwordEncoder, 
                      IJwtTokenProvider tokenProvider,
                      ITokenBlacklistRepository tokenBlacklistRepository,
                      ISesionRepository sesionRepository,
                      SeguridadService seguridadService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.sesionRepository = sesionRepository;
        this.seguridadService = seguridadService;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        var validacion = seguridadService.validarLogin(request.getNombreUsuario());
        if (!validacion.exitoso()) {
            if (validacion.mensaje().contains("inactivo")) {
                throw new UsuarioInactivoException();
            }
            if (validacion.mensaje().contains("bloqueado")) {
                throw new UsuarioBloqueadoException(SeguridadService.getMinutosBloqueo());
            }
            throw new UsuarioNotFoundException(request.getNombreUsuario());
        }
        
        Usuario usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow(() -> new UsuarioNotFoundException(request.getNombreUsuario()));
        
        seguridadService.desbloquearSiEstaBloqueado(usuario);
        
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasenaHash())) {
            seguridadService.registrarIntentoFallido(usuario);
            
            if (usuario.getIntentosFallo() != null && usuario.getIntentosFallo() >= SeguridadService.getMaxIntentosFallidos()) {
                throw new MaximosIntentosExcedidosException(SeguridadService.getMaxIntentosFallidos());
            }
            throw new InvalidCredentialsException();
        }
        
        if (!usuario.getActivo()) {
            throw new UsuarioInactivoException();
        }
        
        seguridadService.registrarIntentoExitoso(usuario);
        
        String token = tokenProvider.generateToken(usuario.getNombreUsuario());
        
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        return LoginResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .rolId(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .area(usuario.getArea() != null ? usuario.getArea().getNombre() : null)
                .build();
    }
    
    @Override
    public LoginResponse refreshToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidCredentialsException();
        }
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (!tokenProvider.validateToken(token)) {
            throw new InvalidCredentialsException();
        }
        
        if (tokenBlacklistRepository.isTokenRevoked(token)) {
            throw new InvalidCredentialsException();
        }
        
        String username = tokenProvider.getUsernameFromToken(token);
        
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsuarioNotFoundException(username));
        
        if (!usuario.getActivo()) {
            throw new UsuarioInactivoException();
        }
        
        seguridadService.registrarIntentoExitoso(usuario);
        
        TokenBlacklist tokenBlacklist = TokenBlacklist.builder()
                .token(token)
                .expiresAt(tokenProvider.getExpirationDate(token))
                .build();
        tokenBlacklistRepository.save(tokenBlacklist);
        
        String newToken = tokenProvider.generateToken(username);
        
        return LoginResponse.builder()
                .token(newToken)
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .area(usuario.getArea() != null ? usuario.getArea().getNombre() : null)
                .build();
    }
    
    @Override
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        LocalDateTime expirationDate = tokenProvider.getExpirationDate(token);
        
        if (expirationDate.isAfter(LocalDateTime.now())) {
            TokenBlacklist tokenBlacklist = TokenBlacklist.builder()
                    .token(token)
                    .expiresAt(expirationDate)
                    .build();
            tokenBlacklistRepository.save(tokenBlacklist);
        }
    }
    
    @Override
    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        boolean tokenNoRevocado = !tokenBlacklistRepository.isTokenRevoked(token);
        boolean tokenValido = tokenProvider.validateToken(token);
        
        return tokenNoRevocado && tokenValido;
    }
    
    @Override
    public List<SesionResponse> getSesionesActivas(String token) {
        String username = getUsernameFromToken(token);
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsuarioNotFoundException(username));
        
        return sesionRepository.findByUsuarioIdAndActivoTrue(usuario.getId())
                .stream()
                .map(s -> SesionResponse.builder()
                        .id(s.getId())
                        .ipAddress(s.getIpAddress())
                        .userAgent(s.getUserAgent())
                        .fechaLogin(s.getFechaLogin())
                        .ultimaActividad(s.getUltimaActividad())
                        .activo(s.getActivo())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public void cerrarSesion(Integer sesionId, String token) {
        String username = getUsernameFromToken(token);
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsuarioNotFoundException(username));
        
        sesionRepository.findByUsuarioIdAndActivoTrue(usuario.getId()).stream()
                .filter(s -> s.getId().equals(sesionId))
                .findFirst()
                .ifPresent(s -> {
                    s.setActivo(false);
                    sesionRepository.save(s);
                });
    }
    
    @Override
    public void cerrarTodasLasSesiones(String token) {
        String username = getUsernameFromToken(token);
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsuarioNotFoundException(username));
        
        sesionRepository.deactivateUserSessions(usuario.getId());
    }
    
    private String getUsernameFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidCredentialsException();
        }
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        return tokenProvider.getUsernameFromToken(token);
    }
}