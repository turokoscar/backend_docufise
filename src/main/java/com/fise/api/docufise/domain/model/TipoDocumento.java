package com.fise.api.docufise.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_tipoDocumento")
    private Integer id;
    
    @Column(name = "txt_nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(name = "txt_descripcion", length = 255)
    private String descripcion;
    
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