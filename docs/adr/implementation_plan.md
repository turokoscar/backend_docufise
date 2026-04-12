# Plan de Desacoplamiento de Datos Hardcoded y Variables de Entorno

Este plan detalla las acciones necesarias para eliminar valores quemados en el código ("hardcoded") y centralizarlos en variables de entorno o archivos de configuración, facilitando la portabilidad y seguridad del sistema DOCUFISE.

## Estado: ✅ COMPLETADO

---

## Resumen de Implementación

### Fase 1: Estandarización del Backend

| Componente | Estado | Archivo |
|------------|--------|---------|
| `.env.example` | ✅ Completo | `backend_docufise/.env.example` |
| `application.yml` placeholders | ✅ Completo | `backend_docufise/src/main/resources/application.yml` |
| CORS y Seguridad externalizados | ✅ Completo | `application.yml` líneas 55-60 |

**Configuraciones externalizadas en `application.yml`:**
- `SERVER_PORT`, `CONTEXT_PATH`
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION`
- `STORAGE_TYPE`, `STORAGE_BASE_PATH`
- `SFTP_*`, `S3_*` (opcionales)
- `MAX_LOGIN_ATTEMPTS`, `LOCKOUT_MINUTES`
- `CORS_ALLOWED_ORIGINS`

### Fase 2: Configuración Dinámica en Frontend

| Componente | Estado | Archivo |
|------------|--------|---------|
| `config.js` | ✅ Completo | `frontend_docufise/src/assets/config/config.js` |
| `ConfigService` | ✅ Completo | `frontend_docufise/src/app/core/services/config.service.ts` |
| `APP_INITIALIZER` | ✅ Completo | `frontend_docufise/src/app/app.config.ts` |
| `ApiService` refactorizado | ✅ Completo | `frontend_docufise/src/app/core/services/api.service.ts` |
| Carga de config.js | ✅ Completo | `frontend_docufise/src/index.html` |

### Fase 3: Validación e Integración

| Componente | Estado | Archivo |
|------------|--------|---------|
| Pruebas de carga | ✅ Verificado | - |

---

## Detalle de Implementación Frontend

### Archivo `config.js`
Ubicación: `frontend_docufise/src/assets/config/config.js`

```javascript
window['__APP_CONFIG__'] = {
  apiUrl: 'http://localhost:8080/api/v1',
  production: false,
  appTitle: 'DocuFISE - Gestión Documental',
  version: '1.0.0'
};
```

Este archivo se carga en `index.html` antes de `<app-root>`.

### Servicio `ConfigService`
Ubicación: `frontend_docufise/src/app/core/services/config.service.ts`

- Usa `APP_INITIALIZER` para cargar configuración antes del bootstrap
- Provee método `get<T>(key)` y `getApiUrl()`
- Valores por defecto si `config.js` no está presente

### Modificaciones Realizadas

1. **`index.html`**: Agregado `<script src="assets/config/config.js"></script>`
2. **`app.config.ts`**: Agregado `configProvider` en providers
3. **`api.service.ts`**: Cambiado de `environment.apiUrl` a `configService.getApiUrl()`

---

## Decisiones Técnicas Tomadas

1. **Backend**: Uso de **`application.yml` con placeholders `${VAR:default}`** para configuración externalizable.
2. **Frontend**: Migración a **Runtime Config** (`config.js`) para permitir cambios de API sin recompilación.

---

## Plan de Verificación

### Pruebas Automatizadas
- [x] `npm run build` en frontend compila correctamente
- [x] `./gradlew build` en backend compila correctamente

### Verificación Manual
1. Modificar `assets/config/config.js` con una URL falsa
2. Recargar el frontend (sin recompilar)
3. Verificar que las peticiones van a la nueva URL

---

## Archivos Creados/Modificados

### Backend
- `backend_docufise/.env.example` (creado)

### Frontend
- `frontend_docufise/src/assets/config/config.js` (creado)
- `frontend_docufise/src/app/core/services/config.service.ts` (creado)
- `frontend_docufise/src/app/app.config.ts` (modificado)
- `frontend_docufise/src/app/core/services/api.service.ts` (modificado)
- `frontend_docufise/src/index.html` (modificado)
