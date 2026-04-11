package com.fise.api.docufise.domain.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "rol_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "rol_id", nullable = false)
    private Integer rolId;
    
    @Column(name = "menu_id", nullable = false)
    private Integer menuId;
    
    @Column(length = 20)
    private String permiso;
}