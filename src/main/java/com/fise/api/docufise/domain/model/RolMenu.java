package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rol_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RolMenuId.class)
public class RolMenu {
    
    @Id
    @Column(name = "ide_rol")
    private Integer ide_rol;
    
    @Id
    @Column(name = "ide_menu")
    private Integer ide_menu;
    
    @Column(name = "txt_permiso", length = 20)
    private String permiso;
    
    @Column(name = "fec_registro")
    private LocalDateTime fec_registro;
    
    @PrePersist
    protected void onCreate() {
        fec_registro = LocalDateTime.now();
    }
}