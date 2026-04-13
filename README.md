# DOCUFISE - Backend API

Sistema Integrado de Gestión de Expedientes y Firmas (SIGEF) - Backend API construido con Spring Boot.

## Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| Java | 17+ | Lenguaje de programación |
| Spring Boot | 3.2.x | Framework principal |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| MySQL | 8.x | Base de datos relacional |
| Gradle | 8.x | Gestión de dependencias |
| JWT | - | Autenticación stateless |
| Lombok | - | Reducción de boilerplate |
| MapStruct | - | Mapeo de objetos |
| Swagger/OpenAPI | 2.x | Documentación de API |

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

Crear una base de datos MySQL:

```sql
CREATE DATABASE docufise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'docufise'@'localhost' IDENTIFIED BY 'docufise_password';
GRANT ALL PRIVILEGES ON docufise.* TO 'docufise'@'localhost';
FLUSH PRIVILEGES;
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
- Rate limiting recomendado a nivel de infraestructura

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

Spring Boot 3.x requiere **Tomcat 10.0+** para Jakarta EE 9+ (espacios de nombres `jakarta.*`). Para usar con **Tomcat 9.0**, se deben realizar ajustes.

### Opción 1: Deploy como WAR externo (Tomcat 9.0)

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

Configurar el contexto en `src/main/resources/application.properties`:

```properties
# Deshabilitar servidor embebido
spring.boot.webapp.context-path=/docufise

# Manejo de excepciones global
server.error.include-message=always
server.error.include-binding-errors=always
```

Crear clase de inicialización `ServletInitializer.java`:

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
```

Copiar el WAR a Tomcat:

```bash
cp build/libs/docufise.war $CATALINA_HOME/webapps/
```

### Opción 2: Usar JAR portable (Recomendado)

Spring Boot genera un JAR executable que incluye Tomcat embebido. Esta es la opción más simple.

```bash
./gradlew bootJar
cp build/libs/docufise-api.jar $CATALINA_HOME/bin/
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
