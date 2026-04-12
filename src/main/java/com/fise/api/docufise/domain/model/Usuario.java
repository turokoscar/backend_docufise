package com.fise.api.docufise.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_usuario")
    private Integer id;
    
    @Column(name = "txt_nombreUsuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;
    
    @Column(name = "txt_contrasenaHash", nullable = false, length = 255)
    private String contrasenaHash;
    
    @Column(name = "txt_nombreCompleto", nullable = false, length = 100)
    private String nombreCompleto;
    
    @Column(name = "txt_correo", nullable = false, unique = true, length = 100)
    private String correo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ide_area")
    private Area area;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ide_rol")
    private Rol rol;
    
    @Column(name = "fec_ultimoLogin")
    private LocalDateTime ultimoLogin;
    
    @Column(name = "num_intentosFallo")
    private Integer intentosFallo = 0;
    
    @Column(name = "fec_bloqueoHasta")
    private LocalDateTime bloqueoHasta;
    
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