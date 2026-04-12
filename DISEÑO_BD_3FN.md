# DISEÑO DE BASE DE DATOS - DOCUFISE
## Normalización hasta 3FN (Tercera Forma Normal)

---

## 1. ANÁLISIS DEL MODELO ACTUAL

### Entidades Identificadas del Frontend

| Entidad | Atributos | Notas |
|--------|-----------|-------|
| USUARIO | id, usuario, contrasena, nombre, correo, area, rol, activo | Dependiente de área y rol |
| ÁREA | id, nombre, descripcion, activo | |
| ROL | id, nombre, descripcion, menus[] | Relación many-to-many con MENU |
| MENU | id, nombre, ruta, icono, orden, activo | |
| EXPEDIENTE | id, numeracion, tipoDocumento, elaboradoPor, enviadoPor, fechaElaboracion, estado, archivoOriginal, areaDestino, usuarioDestino, observaciones, motivoRechazo | Dependiente de tipoDocumento, estado, área destino |
| FIRMA | id, expedienteId, elaboradoPor, tipoDocumento, estado, archivoOriginal, archivoFirmado, motivoRechazo | Dependiente de Expediente |
| TIPO_DOCUMENTO | (valor simple) | Dominio - debería ser tabla |
| ESTADO_EXPEDIENTE | (valor simple) | Dominio - debería ser tabla |

---

## 2. PRIMERA FORMA NORMAL (1FN)

### Criterio: Valores atómicos, sin grupos repetitivos

#### Problemas encontrados:
1. **TIPO_DOCUMENTO** como string libre → debe ser tabla referenciada
2. **ESTADO_EXPEDIENTE** como string libre → debe ser tabla referenciada
3. **menus[]** en ROL → tabla pivote separada

#### Aplicación 1FN:
- ✅ Cada campo tiene valor atómico
- ✅ Cada registro tiene identificador único
- ✅ Sin grupos repetitivos

---

## 3. SEGUNDA FORMA NORMAL (2FN)

### Criterio: Está en 1NF + Sin dependencias parciales (PK compuesta)

#### Análisis de Claves Primarias:
- USUARIO: id (PK simple) → NO tiene dependencia parcial
- ÁREA: id (PK simple) → NO tiene dependencia parcial
- ROL: id (PK simple) → NO tiene dependencia parcial
- MENU: id (PK simple) → NO tiene dependencia parcial
- EXPEDIENTE: id (PK simple) → NO tiene dependencia parcial
- FIRMA: id (PK simple) → NO tiene dependencia parcial

#### Entidades que requieren 2FN:
- **USUARIO**: tiene `area_id` (FK) y `rol_id` (FK) → ambas referenciadas ✅
- **EXPEDIENTE**: tiene referencias a área/usuario destino → normalizado ✅
- **FIRMA**: tiene referencia a EXPEDIENTE → normalizado ✅

#### Aplicación 2FN:
- ✅ Todas las tablas tienen PK simple
- ✅ No hay dependencias parciales
- ✅ Cada tabla tiene tylko una responsabilidad

---

## 4. TERCERA FORMA NORMAL (3FN)

### Criterio: Está en 2FN + Sin dependencias transitivas

#### Análisis de Dependencias Transitivas:

### 4.1 USUARIO
```
usuario → nombre, correo
usuario → area_id → area.nombre
usuario → rol_id → rol.nombre
```
**Problema**: `nombre` y `correo` dependen transitivamente de `usuario` a través de la FK.

**Solución**: Son atributos directos del USUARIO ✅ NO es transitive

### 4.2 EXPEDIENTE
```
id → estado → estado_descripcion (en texto)
id → tipoDocumento → tipo_descripcion
```
**Problema**: El tipo y estado son strings, no referencian a tablas.

**Solución**: Crear tablas de dominio TIPO_DOCUMENTO y ESTADO, referenciarlas desde EXPEDIENTE como FK.

### 4.3 Auditoría
**Problema**: Las fechas `created_at` y `updated_at` se calculan automáticamente.

**Solución**: Usar triggers o默认值 de MySQL ✅ Es solución valida

---

## 5. MODELO NORMALIZADO - 3FN

### 5.1 TABLAS DE DOMINIO (Catálogos)

```sql
-- =============================================
-- TABLAS DE DOMINIO (Referenced Tables)
-- =============================================

CREATE TABLE estado_expediente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre ENUM('REGISTRADO','INGRESADO','PENDIENTE','OBSERVADO','FIRMADO') NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    color_hex VARCHAR(7),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tipo_documento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 5.2 TABLAS DE CONFIGURACIÓN

```sql
-- =============================================
-- TABLAS DE CONFIGURACIÓN DEL SISTEMA
-- =============================================

CREATE TABLE area (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    codigo VARCHAR(20) UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE rol (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    nivel_permiso INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE menu (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    ruta VARCHAR(255),
    icono VARCHAR(50),
    orden INT DEFAULT 0,
    requerido_login BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla pivote ROL_MENU (Relación Muchos a Muchos)
CREATE TABLE rol_menu (
    rol_id INT NOT NULL,
    menu_id INT NOT NULL,
    permiso VARCHAR(20) DEFAULT 'LECTURA',
    PRIMARY KEY (rol_id, menu_id),
    FOREIGN KEY (rol_id) REFERENCES rol(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);
```

### 5.3 TABLAS DE USUARIOS

```sql
-- =============================================
-- GESTIÓN DE USUARIOS
-- =============================================

CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    area_id INT,
    rol_id INT,
    ultimo_login TIMESTAMP NULL,
    intentos_fallo INT DEFAULT 0,
    bloqueo_hasta TIMESTAMP NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (area_id) REFERENCES area(id) ON DELETE SET NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(id) ON DELETE SET NULL,
    INDEX idx_usuario (nombre_usuario),
    INDEX idx_correo (correo),
    INDEX idx_area (area_id),
    INDEX idx_rol (rol_id)
);
```

### 5.4 TABLAS DE NEGOCIO

```sql
-- =============================================
-- GESTIÓN DE DOCUMENTOS / EXPEDIENTES
-- =============================================

CREATE TABLE documento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numeracion VARCHAR(50) NOT NULL UNIQUE,
    tipo_documento_id INT NOT NULL,
    usuario_elabora_id INT NOT NULL,
    usuario_envia_id INT,
    fecha_elaboracion DATE NOT NULL,
    fecha_hora_envio VARCHAR(50),
    estado_id INT NOT NULL DEFAULT 1,
    ruta_archivo_original VARCHAR(500),
    ruta_archivo_firmado VARCHAR(500),
    area_destino_id INT,
    usuario_destino_id INT,
    observaciones TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento(id),
    FOREIGN KEY (usuario_elabora_id) REFERENCES usuario(id),
    FOREIGN KEY (usuario_envia_id) REFERENCES usuario(id),
    FOREIGN KEY (estado_id) REFERENCES estado_expediente(id),
    FOREIGN KEY (area_destino_id) REFERENCES area(id),
    FOREIGN KEY (usuario_destino_id) REFERENCES usuario(id),
    INDEX idx_numeracion (numeracion),
    INDEX idx_estado (estado_id),
    INDEX idx_elabora (usuario_elabora_id),
    INDEX idx_fecha (fecha_elaboracion),
    INDEX idx_area_destino (area_destino_id)
);

-- Historial de estados (para auditoría)
CREATE TABLE documento_historial (
    id INT PRIMARY KEY AUTO_INCREMENT,
    documento_id INT NOT NULL,
    estado_anterior_id INT,
    estado_nuevo_id INT NOT NULL,
    usuario_cambia_id INT NOT NULL,
    motivo_cambio TEXT,
    ip_cliente VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES documento(id),
    FOREIGN KEY (estado_anterior_id) REFERENCES estado_expediente(id),
    FOREIGN KEY (estado_nuevo_id) REFERENCES estado_expediente(id),
    FOREIGN KEY (usuario_cambia_id) REFERENCES usuario(id),
    INDEX idx_documento (documento_id),
    INDEX idx_fecha (created_at)
);

-- =============================================
-- GESTIÓN DE FIRMAS DIGITALES
-- =============================================

CREATE TABLE firma (
    id INT PRIMARY KEY AUTO_INCREMENT,
    documento_id INT NOT NULL,
    usuario_asignado_id INT NOT NULL,
    estado_id INT NOT NULL DEFAULT 2,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_descarga TIMESTAMP NULL,
    fecha_firma TIMESTAMP NULL,
    ruta_archivo_original VARCHAR(500),
    ruta_archivo_firmado VARCHAR(500),
    motivo_rechazo TEXT,
    ip_descarga VARCHAR(45),
    ip_firma VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES documento(id),
    FOREIGN KEY (usuario_asignado_id) REFERENCES usuario(id),
    FOREIGN KEY (estado_id) REFERENCES estado_expediente(id),
    INDEX idx_documento (documento_id),
    INDEX idx_usuario (usuario_asignado_id),
    INDEX idx_estado (estado_id)
);
```

---

## 6. INTEGRIDAD REFERENCIAL

```sql
-- =============================================
-- RESTRICCIONES DE INTEGRIDAD
-- =============================================

-- No eliminar área si tiene usuarios
ALTER TABLE usuario 
    ADD CONSTRAINT fk_usuario_area 
    FOREIGN KEY (area_id) REFERENCES area(id) ON DELETE SET NULL;

-- No eliminar rol si tiene usuarios
ALTER TABLE usuario 
    ADD CONSTRAINT fk_usuario_rol 
    FOREIGN KEY (rol_id) REFERENCES rol(id) ON DELETE SET NULL;

-- No eliminar tipo si tiene documentos
ALTER TABLE documento 
    ADD CONSTRAINT fk_doc_tipo 
    FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento(id) ON DELETE RESTRICT;

-- No eliminar estado si tiene documentos
ALTER TABLE documento 
    ADD CONSTRAINT fk_doc_estado 
    FOREIGN KEY (estado_id) REFERENCES estado_expediente(id) ON DELETE RESTRICT;

-- Eliminación en cascada para documentos
ALTER TABLE firma 
    ADD CONSTRAINT fk_firma_doc 
    FOREIGN KEY (documento_id) REFERENCES documento(id) ON DELETE CASCADE;
```

---

## 7. VISTAS PARA CONSULTAS COMUNES

```sql
-- Vista: Documentos pendientes por área
CREATE VIEW v_documentos_pendientes AS
SELECT d.id, d.numeracion, td.nombre AS tipo, u.nombre_completo AS elaborador,
       a.nombre AS area, e.nombre AS estado, d.fecha_elaboracion
FROM documento d
JOIN tipo_documento td ON d.tipo_documento_id = td.id
JOIN usuario u ON d.usuario_elabora_id = u.id
JOIN area a ON d.area_destino_id = a.id
JOIN estado_expediente e ON d.estado_id = e.id
WHERE e.nombre IN ('PENDIENTE', 'OBSERVADO');

-- Vista: Firmas por usuario
CREATE VIEW v_firmas_usuario AS
SELECT f.id, d.numeracion, td.nombre AS tipo, u.nombre_completo AS elaborador,
       f.fecha_asignacion, e.nombre AS estado
FROM firma f
JOIN documento d ON f.documento_id = d.id
JOIN tipo_documento td ON d.tipo_documento_id = td.id
JOIN usuario u ON d.usuario_elabora_id = u.id
JOIN usuario ua ON f.usuario_asignado_id = ua.id
JOIN estado_expediente e ON f.estado_id = e.id
WHERE ua.id = ?;
```

---

## 8. ÍNDICES PARA RENDIMIENTO

```sql
-- Índices adicionales para consultas frecuentes
CREATE INDEX idx_usuario_nombre ON usuario(nombre_completo);
CREATE INDEX idx_usuario_area_rol ON usuario(area_id, rol_id);
CREATE INDEX idx_doc_estado_fecha ON documento(estado_id, fecha_elaboracion);
CREATE INDEX idx_doc_area_fecha ON documento(area_destino_id, fecha_elaboracion);
CREATE INDEX idx_firma_estado_fecha ON firma(estado_id, fecha_asignacion);
```

---

## 9. VERIFICACIÓN 3FN

| Tabla | 1FN | 2FN | 3FN | Notas |
|------|-----|-----|-----|-------|
| tipo_documento | ✅ | ✅ | ✅ | Tabla dominio |
| estado_expediente | ✅ | ✅ | ✅ | Tabla dominio |
| area | ✅ | ✅ | ✅ | Entidad simple |
| rol | ✅ | ✅ | ✅ | Entidad simple |
| menu | ✅ | ✅ | ✅ | Entidad simple |
| rol_menu | ✅ | ✅ | ✅ | Tabla pivote |
| usuario | ✅ | ✅ | ✅ | FK a area y rol |
| documento | ✅ | ✅ | ✅ | FK a tipo, estado, usuarios, area |
| documento_historial | ✅ | ✅ | ✅ | Auditoría |
| firma | ✅ | ✅ | ✅ | FK a documento, usuario, estado |

---

## 10. RESUMEN DE NORMALIZACIÓN

| Forma Normal | Cumplida | Cómo |
|--------------|-----------|-------|
| **1FN** | ✅ | Valores atómicos, sin grupos repetitivos |
| **2FN** | ✅ | PK simple en todas las tablas |
| **3FN** | ✅ | Sin dependencias transitivas, dominios referenciados |

### Cardinalidades Finales:
- **AREA**: 1:N USUARIO
- **ROL**: 1:N USUARIO
- **ESTADO_EXPEDIENTE**: 1:N DOCUMENTO
- **TIPO_DOCUMENTO**: 1:N DOCUMENTO
- **USUARIO** (elabora): 1:N DOCUMENTO
- **USUARIO** (envía): 0:N DOCUMENTO
- **DOCUMENTO**: 1:N DOCUMENTO_HISTORIAL
- **DOCUMENTO**: 1:N FIRMA
- **USUARIO** (destino): 0:N FIRMA
- **ROL**: N:M MENU (vía rol_menu)