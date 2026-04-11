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
    @Column(name = "ide_firma")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_documento", nullable = false)
    private Documento documento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_usuarioAsignado", nullable = false)
    private Usuario usuarioAsignado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_estado", nullable = false)
    private EstadoExpedienteEntity estado;
    
    @Column(name = "fec_asignacion")
    private LocalDateTime fechaAsignacion;
    
    @Column(name = "fec_descarga")
    private LocalDateTime fechaDescarga;
    
    @Column(name = "fec_firma")
    private LocalDateTime fechaFirma;
    
    @Column(name = "txt_rutaArchivoOriginal", length = 500)
    private String rutaArchivoOriginal;
    
    @Column(name = "txt_rutaArchivoFirmado", length = 500)
    private String rutaArchivoFirmado;
    
    @Column(name = "txt_motivoRechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(name = "txt_ipDescarga", length = 45)
    private String ipDescarga;
    
    @Column(name = "txt_ipFirma", length = 45)
    private String ipFirma;
    
    @Column(name = "fec_registro", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "fec_modificacion")
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