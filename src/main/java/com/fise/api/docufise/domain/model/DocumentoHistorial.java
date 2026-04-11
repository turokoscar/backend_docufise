package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_historial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoHistorial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "documento_id", nullable = false)
    private Integer documentoId;
    
    @Column(name = "estado_anterior_id")
    private Integer estadoAnteriorId;
    
    @Column(name = "estado_nuevo_id", nullable = false)
    private Integer estadoNuevoId;
    
    @Column(name = "usuario_cambia_id", nullable = false)
    private Integer usuarioCambiaId;
    
    @Column(name = "motivo_cambio", columnDefinition = "TEXT")
    private String motivoCambio;
    
    @Column(name = "ip_cliente", length = 45)
    private String ipCliente;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}