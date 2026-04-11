# DOCUFISE - Backend API

## Credenciales de Prueba

| Usuario | Password | Rol |
|---------|----------|-----|
| admin | admin123 | Administrador |
| ctd | admin123 | CTD |
| firmante | admin123 | Firmante |

**Hash BCrypt**: `$2a$10$/UchIoxotfR2xeXzZCr/UeYjPpBRG29MOQXxiq7n3syBvAk/65paa`

## Endpoints de Autenticación

| Método | Endpoint | Descripción |
|--------|----------|------------|
| `POST` | `/api/v1/auth/login` | Iniciar sesión |
| `POST` | `/api/v1/auth/logout` | Cerrar sesión |
| `POST` | `/api/v1/auth/refresh` | Renovar token |
| `GET` | `/api/v1/auth/sesiones` | Listar sesiones activas |
| `DELETE` | `/api/v1/auth/sesiones/{id}` | Cerrar sesión específica |
| `DELETE` | `/api/v1/auth/sesiones` | Cerrar todas las sesiones |

## Ejecutar

```bash
./gradlew bootRun
```

## Swagger UI

Acceder a: http://localhost:8080/swagger-ui.html