package com.fise.api.docufise.domain.ports.input;

import com.fise.api.docufise.shared.dto.LoginRequest;
import com.fise.api.docufise.shared.dto.LoginResponse;
import com.fise.api.docufise.shared.dto.SesionResponse;
import java.util.List;

public interface AuthInputPort {
    
    LoginResponse login(LoginRequest request);
    
    LoginResponse refreshToken(String token);
    
    void logout(String token);
    
    boolean isTokenValid(String token);
    
    List<SesionResponse> getSesionesActivas(String token);
    
    void cerrarSesion(Integer sesionId, String token);
    
    void cerrarTodasLasSesiones(String token);
}