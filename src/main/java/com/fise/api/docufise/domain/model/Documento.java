package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numeracion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_elabora_id", nullable = false)
    private Usuario usuarioElabora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_envia_id")
    private Usuario usuarioEnvia;
    
    @Column(name = "fecha_elaboracion", nullable = false)
    private LocalDate fechaElaboracion;
    
    @Column(name = "fecha_hora_envio", length = 50)
    private String fechaHoraEnvio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoExpedienteEntity estado;
    
    @Column(name = "ruta_archivo_original", length = 500)
    private String rutaArchivoOriginal;
    
    @Column(name = "ruta_archivo_firmado", length = 500)
    private String rutaArchivoFirmado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_destino_id")
    private Area areaDestino;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destino_id")
    private Usuario usuarioDestino;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}