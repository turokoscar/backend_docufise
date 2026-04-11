package com.fise.api.docufise.shared.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es requerido")
    private String nombreUsuario;
    
    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;
}