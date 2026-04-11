package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer usuarioId;
    private String nombreUsuario;
    private String nombreCompleto;
    private String correo;
    private String rol;
    private String area;
}