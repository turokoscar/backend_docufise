package com.fise.api.docufise.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_expediente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoExpedienteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_estadoExpediente")
    private Integer id;
    
    @Column(name = "txt_nombre", nullable = false, unique = true, length = 20)
    private String nombre;
    
    @Column(name = "txt_descripcion", length = 255)
    private String descripcion;
    
    @Column(name = "txt_colorHex", length = 7)
    private String colorHex;
    
    @Column(name = "num_orden")
    private Integer orden;
    
    @Column(name = "flg_activo", nullable = false)
    private Boolean activo = true;
    
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