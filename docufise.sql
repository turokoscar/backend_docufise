# Script SQL - DOCUFISE (Gradle + Spring Boot 2.7.18 + MySQL 8.4)
# Sincronizado con Entidades JPA (Estándar 3FN)

CREATE DATABASE IF NOT EXISTS BD_DOCUFISE_DEV CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BD_DOCUFISE_DEV;

-- =============================================
-- 1. TABLAS DE DOMINIO
-- =============================================
CREATE TABLE estado_expediente (
    ide_estadoExpediente INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombre ENUM('REGISTRADO','INGRESADO','PENDIENTE','OBSERVADO','FIRMADO') NOT NULL UNIQUE,
    txt_descripcion VARCHAR(255),
    txt_colorHex VARCHAR(7),
    num_orden INT DEFAULT 0,
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tipo_documento (
    ide_tipoDocumento INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombre VARCHAR(100) NOT NULL UNIQUE,
    txt_descripcion VARCHAR(255),
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- 2. TABLAS DE CONFIGURACIÓN
-- =============================================
CREATE TABLE area (
    ide_area INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombre VARCHAR(100) NOT NULL UNIQUE,
    txt_descripcion VARCHAR(255),
    txt_codigo VARCHAR(20) UNIQUE,
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE rol (
    ide_rol INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombre VARCHAR(50) NOT NULL UNIQUE,
    txt_descripcion VARCHAR(255),
    num_nivelPermiso INT DEFAULT 0,
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE menu (
    ide_menu INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombre VARCHAR(100) NOT NULL,
    txt_ruta VARCHAR(255),
    txt_icono VARCHAR(50),
    num_orden INT DEFAULT 0,
    flg_requeridoLogin BOOLEAN DEFAULT TRUE,
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE rol_menu (
    ide_rol INT NOT NULL,
    ide_menu INT NOT NULL,
    txt_permiso VARCHAR(20) DEFAULT 'LECTURA',
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ide_rol, ide_menu),
    FOREIGN KEY (ide_rol) REFERENCES rol(ide_rol) ON DELETE CASCADE,
    FOREIGN KEY (ide_menu) REFERENCES menu(ide_menu) ON DELETE CASCADE
);

-- =============================================
-- 3. USUARIOS
-- =============================================
CREATE TABLE usuario (
    ide_usuario INT PRIMARY KEY AUTO_INCREMENT,
    txt_nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    txt_contrasenaHash VARCHAR(255) NOT NULL,
    txt_nombreCompleto VARCHAR(100) NOT NULL,
    txt_correo VARCHAR(100) NOT NULL UNIQUE,
    ide_area INT,
    ide_rol INT,
    fec_ultimoLogin TIMESTAMP NULL,
    num_intentosFallo INT DEFAULT 0,
    fec_bloqueoHasta TIMESTAMP NULL,
    flg_activo BOOLEAN DEFAULT TRUE,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ide_area) REFERENCES area(ide_area) ON DELETE SET NULL,
    FOREIGN KEY (ide_rol) REFERENCES rol(ide_rol) ON DELETE SET NULL,
    INDEX idx_usuario (txt_nombreUsuario),
    INDEX idx_correo (txt_correo)
);

-- =============================================
-- 4. DOCUMENTOS / EXPEDIENTES
-- =============================================
CREATE TABLE documento (
    ide_documento INT PRIMARY KEY AUTO_INCREMENT,
    txt_numeracion VARCHAR(50) NOT NULL UNIQUE,
    ide_tipoDocumento INT NOT NULL,
    ide_usuarioElabora INT NOT NULL,
    ide_usuarioEnvia INT,
    fec_elaboracion DATE NOT NULL,
    fec_envio VARCHAR(50),
    ide_estado INT NOT NULL DEFAULT 1,
    txt_rutaArchivoOriginal VARCHAR(500),
    txt_rutaArchivoFirmado VARCHAR(500),
    ide_areaDestino INT,
    ide_usuarioDestino INT,
    txt_observaciones TEXT,
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ide_tipoDocumento) REFERENCES tipo_documento(ide_tipoDocumento),
    FOREIGN KEY (ide_usuarioElabora) REFERENCES usuario(ide_usuario),
    FOREIGN KEY (ide_usuarioEnvia) REFERENCES usuario(ide_usuario),
    FOREIGN KEY (ide_estado) REFERENCES estado_expediente(ide_estadoExpediente),
    FOREIGN KEY (ide_areaDestino) REFERENCES area(ide_area),
    FOREIGN KEY (ide_usuarioDestino) REFERENCES usuario(ide_usuario),
    INDEX idx_numeracion (txt_numeracion),
    INDEX idx_estado (ide_estado),
    INDEX idx_fecha (fec_elaboracion)
);

-- =============================================
-- 5. FIRMAS DIGITALES
-- =============================================
CREATE TABLE firma (
    ide_firma INT PRIMARY KEY AUTO_INCREMENT,
    ide_documento INT NOT NULL,
    ide_usuarioAsignado INT NOT NULL,
    ide_estado INT NOT NULL DEFAULT 2,
    fec_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_descarga TIMESTAMP NULL,
    fec_firma TIMESTAMP NULL,
    txt_rutaArchivoOriginal VARCHAR(500),
    txt_rutaArchivoFirmado VARCHAR(500),
    txt_motivoRechazo TEXT,
    txt_ipDescarga VARCHAR(45),
    txt_ipFirma VARCHAR(45),
    fec_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fec_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ide_documento) REFERENCES documento(ide_documento) ON DELETE CASCADE,
    FOREIGN KEY (ide_usuarioAsignado) REFERENCES usuario(ide_usuario),
    FOREIGN KEY (ide_estado) REFERENCES estado_expediente(ide_estadoExpediente),
    INDEX idx_documento (ide_documento),
    INDEX idx_usuario (ide_usuarioAsignado),
    INDEX idx_estado (ide_estado)
);

-- =============================================
-- 6. SEED DATA - Datos Iniciales
-- =============================================
-- Estados
INSERT INTO estado_expediente (txt_nombre, txt_descripcion, txt_colorHex, num_orden) VALUES
('REGISTRADO', 'Documento registrado por elaborador', '#3B7DCC', 1),
('INGRESADO', 'Documento derivado a área destino', '#2C5AAB', 2),
('PENDIENTE', 'Documento descargado para firma', '#F2B801', 3),
('OBSERVADO', 'Documento observado/rechazado', '#AB2741', 4),
('FIRMADO', 'Documento firmado electrónicamente', '#0FBF90', 5);

-- Tipos de documento
INSERT INTO tipo_documento (txt_nombre, txt_descripcion) VALUES
('Carta', 'Comunicación formal escrita'),
('Informe', 'Documento de análisis o reporte'),
('Resolución', 'Decisión administrativa'),
('Memorándum', 'Comunicación interna'),
('Circular', 'Comunicación múltiple');

-- Áreas
INSERT INTO area (txt_nombre, txt_descripcion, txt_codigo) VALUES
('Secretaría General', 'Órgano administrativo central', 'SEC'),
('Área Legal', 'Asesoría jurídica', 'LEG'),
('Área Contable', 'Gestión financiera', 'CON'),
('Recursos Humanos', 'Gestión de personal', 'RRHH');

-- Roles
INSERT INTO rol (txt_nombre, txt_descripcion, num_nivelPermiso) VALUES
('CTD', 'Centro de Trámite Documentario', 1),
('Firmante', 'Usuario que firma documentos', 2),
('Administrador', 'Acceso total al sistema', 3);

-- Menús (para el sidebar)
INSERT INTO menu (txt_nombre, txt_ruta, txt_icono, num_orden) VALUES
('Inicio', '/', 'Home', 1),
('Expedientes', '/expedientes', 'FileText', 2),
('Firmas', '/firmas', 'PenTool', 3),
('Reportes', '/reportes', 'BarChart', 4),
('Gestión de Usuarios', '/admin/usuarios', 'Users', 5),
('Gestión de Áreas', '/admin/areas', 'Building', 6),
('Gestión de Roles', '/admin/roles', 'Shield', 7),
('Gestión de Menús', '/admin/menus', 'Menu', 8);

-- Asignación de menús a roles (cada rol solo ve lo que necesita)
-- CTD: Solo Expedientes
-- Firmante: Solo Firmas  
-- Administrador: Todos
INSERT INTO rol_menu (ide_rol, ide_menu, txt_permiso) VALUES
-- CTD (rol 1): Solo Expedientes
(1, 2, 'ESCRITURA'),
-- Firmante (rol 2): Solo Firmas
(2, 3, 'ESCRITURA'),
-- Administrador (rol 3): Todos los menús
(3, 1, 'ESCRITURA'), (3, 2, 'ESCRITURA'), (3, 3, 'ESCRITURA'), 
(3, 4, 'ESCRITURA'), (3, 5, 'ESCRITURA'), (3, 6, 'ESCRITURA'), 
(3, 7, 'ESCRITURA'), (3, 8, 'ESCRITURA');

-- Usuarios (password: admin123)
INSERT INTO usuario (txt_nombreUsuario, txt_contrasenaHash, txt_nombreCompleto, txt_correo, ide_area, ide_rol) VALUES
('admin', '$2a$10$/UchIoxotfR2xeXzZCr/UeYjPpBRG29MOQXxiq7n3syBvAk/65paa', 'Administrador Sistema', 'admin@fise.gob', 1, 3),
('ctd', '$2a$10$/UchIoxotfR2xeXzZCr/UeYjPpBRG29MOQXxiq7n3syBvAk/65paa', 'Usuario CTD', 'ctd@fise.gob', 1, 1),
('firmante', '$2a$10$/UchIoxotfR2xeXzZCr/UeYjPpBRG29MOQXxiq7n3syBvAk/65paa', 'Usuario Firmante', 'firmante@fise.gob', 2, 2);

-- Verificación
SELECT 'Base de datos creada' AS mensaje;