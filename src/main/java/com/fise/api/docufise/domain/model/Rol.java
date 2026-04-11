package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_rol")
    private Integer id;
    
    @Column(name = "txt_nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(name = "txt_descripcion", length = 255)
    private String descripcion;
    
    @Column(name = "num_nivelPermiso")
    private Integer nivelPermiso;
    
    @Column(name = "flg_activo", nullable = false)
    private Boolean activo = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rol_menu",
        joinColumns = @JoinColumn(name = "ide_rol"),
        inverseJoinColumns = @JoinColumn(name = "ide_menu")
    )
    private List<Menu> menus;
    
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