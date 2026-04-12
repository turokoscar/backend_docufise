# PLAN DE IMPLEMENTACIÓN - BACKEND DOCUFISE

## 1. ANÁLISIS DEL FRONTEND EXISTENTE

### Modelos de Datos (TypeScript → Java)
```typescript
// Frontend actual basado en user.model.ts
- EstadoExpediente: REGISTRADO | INGRESADO | PENDIENTE | OBSERVADO | FIRMADO
- RolUsuario: CTD | Firmante | Administrador
- UsuarioSistema: id, usuario, contrasena, nombre, correo, area, rol, activo
- AreaSistema: id, nombre, descripcion, activo
- RolSistema: id, nombre, descripcion, menus[]
- MenuSistema: id, nombre, ruta, icono, orden, activo
- Expediente: id, numeracion, tipoDocumento, elaboradoPor, enviadoPor, fechaElaboracion, 
              fechaHoraEnvio, estado, archivoOriginal, archivoFirmado, areaDestino, 
              usuarioDestino, observaciones, motivoRechazo
- Firma: id, expedienteId, elaboradasPor, tipoDocumento, estado, fechaHora, 
         archivoOriginal, archivoFirmado, motivoRechazo
```

### Endpoints Esperados (del frontend)
- Login: POST `/api/auth/login`
- Expedientes: GET/POST/PUT/DELETE `/api/expedientes`
- Firmas: GET/POST `/api/firmas`
- Derivación: POST `/api/expedientes/{id}/derivar`
- Descarga: GET `/api/expedientes/{id}/descargar`
- Firmar: POST `/api/firmas/{id}/firmar`
- Rechazar: POST `/api/firmas/{id}/rechazar`
- Reportes: GET `/api/reportes/...`
- Áreas: GET/POST `/api/areas`
- Roles: GET/POST `/api/roles`
- Usuarios: GET/POST `/api/usuarios`
- Menús: GET/POST `/api/menus`

---

## 2. STACK TECNOLÓGICO

| Componente | Versión | Justificación |
|------------|---------|----------------|
| Java SDK | 17 | LTS actual, mejor rendimiento |
| Spring Boot | 2.7.18 | LTS compatible, estable |
| MySQL | 8.4.0 | Soporte JSON, mejor rendimiento |
| Gradle | 8.5+ | Build tool moderno |
| JPA/Hibernate | 5.x | ORM estándar |
| Spring Security | 5.7.x | Autenticación JWT |
| Lombok | - |减少 boilerplate |

---

## 3. ARQUITECTURA DEL PROYECTO

```
backend_docufise/
├── build.gradle          # Configuración Gradle
├── settings.gradle
├── gradle.properties
├── src/main/java/com/fise/docufise/
│   ├── DocufiseApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── CorsConfig.java
│   │   └── DataSourceConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── ExpedienteController.java
│   │   ├── FirmaController.java
│   │   ├── ReporteController.java
│   │   └── admin/
│   │       ├── UsuarioController.java
│   │       ├── AreaController.java
│   │       ├── RolController.java
│   │       └── MenuController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── ExpedienteService.java
│   │   ├── FirmaService.java
│   │   └── ...
│   ├── repository/
│   │   ├── ExpedienteRepository.java
│   │   ├── FirmaRepository.java
│   │   └── ...
│   ├── model/
│   │   ├── entity/
│   │   │   ├── Usuario.java
│   │   │   ├── Expediente.java
│   │   │   ├── Firma.java
│   │   │   └── ...
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       ├── ExpedienteRequest.java
│   │       └── ...
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   ├── application.yml
│   └── application-dev.yml
└── src/test/java/
```

---

## 4. BASE DE DATOS

### Esquema MySQL
```sql
CREATE DATABASE docufise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE docufise;

-- Tablas del sistema
CREATE TABLE areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    area_id BIGINT,
    rol_id BIGINT,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (area_id) REFERENCES areas(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE menus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruta VARCHAR(255),
    icono VARCHAR(50),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tablas de negocio
CREATE TABLE documentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numeracion VARCHAR(50) NOT NULL UNIQUE,
    tipo_documento VARCHAR(100) NOT NULL,
    elaborado_por VARCHAR(100) NOT NULL,
    enviado_por VARCHAR(100),
    fecha_elaboracion DATE NOT NULL,
    fecha_hora_envio VARCHAR(50),
    estado ENUM('REGISTRADO','INGRESADO','PENDIENTE','OBSERVADO','FIRMADO') DEFAULT 'REGISTRADO',
    archivo_original VARCHAR(255),
    archivo_firmado VARCHAR(255),
    area_destino VARCHAR(100),
    usuario_destino VARCHAR(100),
    observaciones TEXT,
    motivo_rechazo TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_estado (estado),
    INDEX idx_elaborado_por (elaborado_por),
    INDEX idx_fecha (fecha_elaboracion)
);

CREATE TABLE firmas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    documento_id BIGINT NOT NULL,
    elaborador VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(100) NOT NULL,
    estado ENUM('REGISTRADO','INGRESADO','PENDIENTE','OBSERVADO','FIRMADO') DEFAULT 'INGRESADO',
    fecha_hora VARCHAR(50) NOT NULL,
    archivo_original VARCHAR(255),
    archivo_firmado VARCHAR(255),
    motivo_rechazo TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES documentos(id),
    INDEX idx_estado (estado),
    INDEX idx_elaborador (elaborador)
);

-- Tabla pivot paraRoles-Menus
CREATE TABLE roles_menus (
    rol_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (rol_id, menu_id),
    FOREIGN KEY (rol_id) REFERENCES roles(id),
    FOREIGN KEY (menu_id) REFERENCES menus(id)
);
```

---

## 5. IMPLEMENTACIÓN - FASES

### FASE 1:Configuración.Inicial (Semana 1)
- [ ] Crear proyecto Spring Boot 2.7.18
- [ ] Configurar pom.xml con dependencias
- [ ] Configurar application.yml
- [ ] Configurar CORS
- [ ] Configurar Seguridad JWT
- [ ] Crear tablas en MySQL

### FASE 2: Autenticación (Semana 1-2)
- [ ] Entity Usuario + Repository
- [ ] DTO LoginRequest/LoginResponse
- [ ] AuthController con JWT
- [ ] Login logout endpoints
- [ ] Proteger endpoints

### FASE 3: Gestión Expedientes (Semana 2-3)
- [ ] Entity Expediente + Repository
- [ ] ExpedienteController CRUD
- [ ] Derivación endpoint
- [ ] Validar flujo estados
- [ ] Subida/descarga archivos

### FASE 4: Gestión Firmas (Semana 3)
- [ ] Entity Firma + Repository
- [ ] FirmaController
- [ ] Descargar documento
- [ ] Firmar documento
- [ ] Rechazar con motivo

### FASE 5: Reportes (Semana 3-4)
- [ ] ReporteController
- [ ] Endpoints estadísticas
- [ ] Conteo por estado

### FASE 6: Administración (Semana 4)
- [ ] CRUD Áreas
- [ ] CRUD Roles
- [ ] CRUD Usuarios
- [ ] CRUD Menús
- [ ] Asignación Roles-Menus

---

## 6. API REST - ENDPOINTS

### Autenticación
```
POST /api/auth/login     - Login usuario
POST /api/auth/logout   - Logout usuario
GET  /api/auth/me       - Usuario actual
```

### Expedientes
```
GET    /api/expedientes            - Listar (paginado)
GET    /api/expedientes/{id}      - Ver detalle
POST   /api/expedientes          - Crear
PUT    /api/expedientes/{id}     - Actualizar
DELETE /api/expedientes/{id}      - Eliminar
POST   /api/expedientes/{id}/derivar - Derivar a área
GET    /api/expedientes/export    - Exportar Excel
```

### Firmas
```
GET    /api/firmas               - Lista usuario
GET    /api/firmas/{id}         - Ver detalle
POST   /api/firmas/{id}/descargar - Descargar doc
POST   /api/firmas/{id}/firmar  - Firmar doc
POST   /api/firmas/{id}/rechazar - Rechazar con motivo
```

### Reportes
```
GET /api/reportes/resumen      - Resumen general
GET /api/reportes/estados     - Conteo por estado
GET /api/reportes/tendencias  - Datos gráficos
```

### Administración
```
GET/POST /api/admin/usuarios   - CRUD Usuarios
GET/POST /api/admin/areas     - CRUD Áreas
GET/POST /api/admin/roles     - CRUD Roles
GET/POST /api/admin/menus     - CRUD Menús
```

---

## 7. SEGURIDAD

### JWT Configuración
- Algorithm: HS256
- Secret: Configurar en application.yml
- Expiration: 24 horas
- Refresh Token: 7 días

### Roles y Permisos
| Ruta | CTD | Firmante | Administrador |
|------|-----|---------|--------------|
| /api/expedientes/* | DUEÑO | READ | ALL |
| /api/firmas/* | READ | DUEÑO | ALL |
| /api/reportes/* | READ | READ | ALL |
| /api/admin/* | - | - | ALL |

---

## 8. ARCHIVOS

### Almacenamiento
- Directorio: `/uploads/documentos/`
- Estructura:
  ```
  /uploads/
  ├── originales/
  │   └── {año}/
  │       └── {mes}/
  │           └── {documento_id}.pdf
  └── firmados/
      └── {año}/
          └── {mes}/
              └── {documento_id}_firmado.pdf
  ```

---

## 9. TESTS

### Unitarios
- Service layer con JUnit 5
- Repository con H2 embebido

### Integración
- Controladores con MockMvc

---

## 10. VARIABLES DE ENTORNO

```yaml
# application.yml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/docufise}
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:root}
  jpa:
    show-sql: ${DEBUG:false}

server:
  port: ${PORT:8080}

jwt:
  secret: ${JWT_SECRET:docufise-secret-key-2026}
  expiration: ${JWT_EXPIRATION:86400000}

file:
  upload-dir: ${UPLOAD_DIR:/uploads}
```

---

## PRIORIDADES DE IMPLEMENTACIÓN

1. **CRÍTICO**: Login + JWT
2. **CRÍTICO**: Expedientes CRUD + Derivación
3. **CRÍTICO**: Firmas +Workflow
4. **ALTO**: Reportes
5. **MEDIO**: Administración