package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_menu")
    private Integer id;
    
    @Column(name = "txt_nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "txt_ruta", length = 255)
    private String ruta;
    
    @Column(name = "txt_icono", length = 50)
    private String icono;
    
    @Column(name = "num_orden")
    private Integer orden;
    
    @Column(name = "flg_requeridoLogin")
    private Boolean requeridoLogin = true;
    
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