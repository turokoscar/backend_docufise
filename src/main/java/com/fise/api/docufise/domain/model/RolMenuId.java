package com.fise.api.docufise.domain.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolMenuId implements Serializable {
    private Integer ide_rol;
    private Integer ide_menu;
}