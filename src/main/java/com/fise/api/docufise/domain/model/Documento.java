package com.fise.api.docufise.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Documento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_documento")
    private Integer id;
    
    @Column(name = "txt_numeracion", nullable = false, unique = true, length = 50)
    private String numeracion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_tipoDocumento", nullable = false)
    private TipoDocumento tipoDocumento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_usuarioElabora", nullable = false)
    private Usuario usuarioElabora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_usuarioEnvia")
    private Usuario usuarioEnvia;
    
    @Column(name = "fec_elaboracion", nullable = false)
    private LocalDate fechaElaboracion;
    
    @Column(name = "fec_envio", length = 50)
    private String fechaHoraEnvio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_estado", nullable = false)
    private EstadoExpedienteEntity estado;
    
    @Column(name = "txt_rutaArchivoOriginal", length = 500)
    private String rutaArchivoOriginal;
    
    @Column(name = "txt_rutaArchivoFirmado", length = 500)
    private String rutaArchivoFirmado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_areaDestino")
    private Area areaDestino;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ide_usuarioDestino")
    private Usuario usuarioDestino;
    
    @Column(name = "txt_observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fec_registro", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "fec_modificacion")
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