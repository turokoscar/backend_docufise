package com.fise.api.docufise.shared.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionResponse {
    private Integer id;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime fechaLogin;
    private LocalDateTime ultimaActividad;
    private Boolean activo;
}