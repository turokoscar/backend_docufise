package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "firma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Firma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_asignado_id", nullable = false)
    private Usuario usuarioAsignado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoExpedienteEntity estado;
    
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;
    
    @Column(name = "fecha_descarga")
    private LocalDateTime fechaDescarga;
    
    @Column(name = "fecha_firma")
    private LocalDateTime fechaFirma;
    
    @Column(name = "ruta_archivo_original", length = 500)
    private String rutaArchivoOriginal;
    
    @Column(name = "ruta_archivo_firmado", length = 500)
    private String rutaArchivoFirmado;
    
    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(name = "ip_descarga", length = 45)
    private String ipDescarga;
    
    @Column(name = "ip_firma", length = 45)
    private String ipFirma;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        fechaAsignacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}