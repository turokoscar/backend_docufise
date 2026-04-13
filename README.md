# DOCUFISE - Backend API

Sistema Integrado de Gestión de Expedientes y Firmas (SIGEF) - Backend API construido con Spring Boot.

## Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| Java | 17+ | Lenguaje de programación |
| Spring Boot | 2.7.x | Framework principal |
| Spring Security | 5.x | Autenticación y autorización |
| Spring Data JPA | 2.7.x | Persistencia de datos |
| MySQL | 8.x | Base de datos relacional |
| Gradle | 8.x | Gestión de dependencias |
| JWT | - | Autenticación stateless |
| Lombok | 1.18.x | Reducción de boilerplate |
| Swagger/OpenAPI | 1.7.x | Documentación de API |

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal** (Ports & Adapters):

```
├── src/main/java/com/fise/api/docufise/
│   ├── domain/           # Núcleo del negocio
│   │   ├── model/        # Entidades de dominio
│   │   ├── ports/        # Interfaces (input/output)
│   │   └── service/      # Servicios de dominio
│   ├── application/      # Casos de uso
│   │   └── service/     # Servicios de aplicación
│   ├── infrastructure/  # Adaptadores externos
│   │   ├── controller/   # Controladores REST
│   │   ├── repository/  # Repositorios JPA
│   │   ├── security/    # Configuración de seguridad
│   │   └── dto/        # Objetos de transferencia
│   └── shared/          # Utilidades compartidas
│       ├── config/      # Configuraciones
│       ├── dto/         # DTOs genéricos
│       └── exception/    # Excepciones personalizadas
```

## Requisitos

- **Java Development Kit (JDK)** 17 o superior
- **MySQL** 8.0 o superior
- **Gradle** 8.x (o usar el wrapper incluido)
- **Git**

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd backend_docufise
```

### 2. Configurar la base de datos

El proyecto incluye un script SQL completo en la raíz del proyecto: **`docufise.sql`**

Este script crea:
- Base de datos `BD_DOCUFISE_DEV`
- Todas las tablas del sistema
- Datos iniciales (estados, tipos de documento, áreas, roles, menús, usuarios)

**Ejecutar con MySQL 8.4:**

```bash
# Conectar a MySQL como root
mysql -u root -p

# Ejecutar el script
source docufise.sql
```

O directamente desde terminal:

```bash
mysql -u root -p < docufise.sql
```

**Verificar creación:**

```sql
USE BD_DOCUFISE_DEV;
SHOW TABLES;
SELECT * FROM usuario;
```

**Datos iniciales creados:**

| Tabla | Descripción |
|-------|-------------|
| `estado_expediente` | Estados: REGISTRADO, INGRESADO, PENDIENTE, OBSERVADO, FIRMADO |
| `tipo_documento` | Tipos: Carta, Informe, Resolución, Memorándum, Circular |
| `area` | Secretaría General, Área Legal, Contable, RRHH |
| `rol` | CTD, Firmante, Administrador |
| `menu` | 8 opciones de menú |
| `rol_menu` | Permisos por rol |
| `usuario` | 3 usuarios de prueba (admin, ctd, firmante) |

**Reset de base de datos:**

```bash
mysql -u root -p -e "DROP DATABASE IF EXISTS BD_DOCUFISE_DEV;"
mysql -u root -p < docufise.sql
```

### 3. Configurar variables de entorno

Crear un archivo `.env` en la raíz del proyecto (opcional, para desarrollo local):

```env
# Base de datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=docufise
DB_USER=docufise
DB_PASSWORD=docufise_password

# JWT
JWT_SECRET=your-super-secret-key-minimum-256-bits-for-hs256
JWT_EXPIRATION_MS=86400000

# Servidor
SERVER_PORT=8080
```

### 4. Ejecutar la aplicación

Usando Gradle wrapper:

```bash
./gradlew bootRun
```

O compilar y ejecutar el JAR:

```bash
./gradlew build
java -jar build/libs/docufise-api.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Credenciales de Prueba

| Usuario | Password | Rol | Descripción |
|---------|----------|-----|-------------|
| `admin` | `admin123` | ADMIN | Administrador del sistema |
| `ctd` | `admin123` | CTD | Centro de Trámite Documentario |
| `firmante` | `admin123` | FIRMANTE | Usuario firmante de documentos |

## Endpoints Principales

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/login` | Iniciar sesión |
| `POST` | `/api/v1/auth/logout` | Cerrar sesión |
| `POST` | `/api/v1/auth/refresh` | Renovar token JWT |
| `GET` | `/api/v1/auth/sesiones` | Listar sesiones activas |
| `DELETE` | `/api/v1/auth/sesiones/{id}` | Cerrar sesión específica |

### Documentos/Expedientes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/documentos` | Listar documentos |
| `POST` | `/api/v1/documentos` | Crear documento |
| `GET` | `/api/v1/documentos/{id}` | Obtener documento |
| `PUT` | `/api/v1/documentos/{id}` | Actualizar documento |
| `DELETE` | `/api/v1/documentos/{id}` | Eliminar documento |
| `GET` | `/api/v1/documentos/estadisticas` | Estadísticas de documentos |

### Firmas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/v1/firmas` | Listar firmas |
| `POST` | `/api/v1/firmas` | Crear solicitud de firma |
| `PUT` | `/api/v1/firmas/{id}/firmar` | Firmar documento |
| `GET` | `/api/v1/firmas/{id}/descargar` | Descargar documento |
| `GET` | `/api/v1/firmas/{id}/descargar-firmado` | Descargar documento firmado |

### Administración

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET/POST` | `/api/v1/usuarios` | Gestión de usuarios |
| `GET/POST` | `/api/v1/areas` | Gestión de áreas |
| `GET/POST` | `/api/v1/roles` | Gestión de roles |
| `GET/POST` | `/api/v1/menus` | Gestión de menús |

## Documentación de API

Swagger UI disponible en: `http://localhost:8080/swagger-ui.html`

## Seguridad

- Autenticación mediante **JWT** (JSON Web Tokens)
- Contraseñas hasheadas con **BCrypt**
- Tokens de refresh para sesiones prolongadas
- CORS configurado para orígenes específicos
- Spring Security 5.x (compatible con Spring Boot 2.7.x)

## Estructura de Respuestas API

### Respuesta exitosa

```json
{
  "exitoso": true,
  "datos": { ... },
  "mensaje": "Operación exitosa"
}
```

### Respuesta de error

```json
{
  "exitoso": false,
  "error": {
    "codigo": "ERROR_CODE",
    "mensaje": "Descripción del error"
  }
}
```

## Comandos de Desarrollo

```bash
# Compilar
./gradlew compileJava

# Ejecutar tests
./gradlew test

# Ejecutar con hot-reload (Spring Boot DevTools)
./gradlew bootRun

# Limpiar build
./gradlew clean

# Ver dependencias
./gradlew dependencies

# Generar JAR
./gradlew bootJar
```

## Despliegue en Apache Tomcat 9.0

Spring Boot 2.7.x es **compatible de forma nativa con Tomcat 9.0**. Se puede usar el JAR embebido o deployar como WAR.

### Opción 1: JAR embebido (Recomendado)

Spring Boot incluye Tomcat embebido. Generar JAR y ejecutar:

```bash
./gradlew bootJar
cp build/libs/docufise-api-1.0.0.jar $CATALINA_HOME/webapps/
```

### Opción 2: Deploy como WAR externo

Modificar `build.gradle` para generar un WAR:

```groovy
apply plugin: 'war'

dependencies {
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
}

bootWar {
    archiveFileName = 'docufise.war'
}
```

Crear clase de inicialización `ServletInitializer.java` (si no existe):

```java
package com.fise.api.docufise;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiDocufiseApplication.class);
    }
}
```

Generar el WAR:

```bash
./gradlew bootWar
cp build/libs/docufise.war $CATALINA_HOME/webapps/
```

Configurar `setenv.sh` (crear si no existe):

```bash
#!/bin/bash
CATALINA_OPTS="$CATALINA_OPTS -Xms512m -Xmx2048m"
CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=production"
CATALINA_OPTS="$CATALINA_OPTS -Dserver.port=8080"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"
```

### Configuración de Producción

Variables de entorno recomendadas:

```bash
# Base de datos
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=docufise
export DB_USER=docufise
export DB_PASSWORD=secure_password

# JWT
export JWT_SECRET=your-256-bit-secret-key-here
export JWT_EXPIRATION_MS=86400000

# Servidor
export SERVER_PORT=8080
export SERVER_CONTEXT_PATH=/docufise
```

### Configuración CORS para Producción

En `SecurityConfig.java`, ajustar orígenes permitidos:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://192.168.1.100:80",
        "http://servidor.dominio.com"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### Verificación del Despliegue

```bash
# Ver logs de Tomcat
tail -f $CATALINA_HOME/logs/catalina.out

# Verificar que la aplicación está corriendo
curl http://localhost:8080/api/v1/auth/login

# Health check
curl http://localhost:8080/actuator/health
```

### Estructura de Directorios Recomendada

```
/opt/
├── docufise/
│   ├── backend/
│   │   ├── docufise.jar
│   │   ├── config/
│   │   │   └── application-prod.properties
│   │   └── logs/
│   └── frontend/
│       └── dist/
└── tomcat/
    └── webapps/
        └── docufise.war (si se usa WAR)
```

## Licencia

Propiedad de FISE - Todos los derechos reservados.
